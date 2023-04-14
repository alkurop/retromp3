package com.omar.retromp3recorder.iorecorder

import android.media.AudioRecord
import android.os.Build
import android.os.Process
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.recorder.LameModule
import com.omar.retromp3recorder.io.recorder.R
import com.omar.retromp3recorder.iorecorder.RecorderObserverFlow.sendFinishLog
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import com.omar.retromp3recorder.utils.domain.chainSuspend
import com.omar.retromp3recorder.utils.domain.combineWith
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

class Mp3VoiceRecorderLame @Inject internal constructor(
    private val audioCoroutineContext: AudioCoroutineContext
) : Mp3VoiceRecorder {

    private val events = MutableSharedFlow<Mp3VoiceRecorder.Event>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        replay = 1
    )
    private val elapsed = AtomicLong(0)
    private val recorderBus = MutableSharedFlow<ShortArray>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        replay = 1
    )
    private val state = MutableStateFlow(Mp3VoiceRecorder.State.Idle)

    override fun stateFlow(): Flow<Mp3VoiceRecorder.State> = state

    override fun recorderFlow(): Flow<ByteArray> = RecorderObserverFlow.map(recorderBus)

    override fun eventsFlow(): Flow<Mp3VoiceRecorder.Event> = events

    override fun recordWithProps(props: Mp3VoiceRecorder.RecorderProps) {
        val sampleRate = props.prefs.sampleRate.value
        val bitRate = props.prefs.bitRate.value
        val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, encoding)
        audioCoroutineContext.cancel()
        audioCoroutineContext.launch {
            state.emit(Mp3VoiceRecorder.State.Recording)
            val audioSource = props.audioSourcePref
            val audioRecord = when {
                audioSource is Mp3VoiceRecorder.AudioSource.Output && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                    findAudioRecordForMediaProjection(
                        audioSource.mediaProjection,
                        audioSource.source,
                        minBufferSize,
                        sampleRate = sampleRate
                    )
                else -> findAudioRecordForMic(
                    minBufferSize = minBufferSize,
                    sampleRate = sampleRate
                )
            }

            val recorderResult = audioRecord
                .chainSuspend {
                    initRecorder(
                        sampleRate = sampleRate,
                        bitRate = bitRate,
                        audioRecord = it
                    )
                }
                .combineWith(createOutputFile(props.filepath))
                .chainSuspend {
                    record(it.second, it.first, sampleRate)
                }

            state.emit(Mp3VoiceRecorder.State.Idle)

            if (recorderResult.isFailure) {
                val error = recorderResult.exceptionOrNull()!!
                events.emit(
                    Mp3VoiceRecorder.Event.Error(
                        Stringer.ofString(error.message ?: error.toString())
                    )
                )
            }
        }
    }

    override fun stopRecord() {
        state.tryEmit(Mp3VoiceRecorder.State.Idle)
    }

    suspend fun record(outputFile: File, recorder: AudioRecord, sampleRate: Int): Result<Unit> {
        val stream = outputFile.outputStream()
        return try {
            elapsed.set(System.currentTimeMillis())
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            val buffer = ShortArray(sampleRate)
            val mp3Buffer = createMp3Buffer(buffer)
            val minBufferSize =
                AudioRecord.getMinBufferSize(sampleRate, channelConfig, encoding)
            recorder.startRecording()
            var readSize: Int
            while (state.first() == Mp3VoiceRecorder.State.Recording) {
                readSize = recorder.read(buffer, 0, minBufferSize)
                //left and right channel goes into one
                val encResult = LameModule.encode(buffer, buffer, readSize, mp3Buffer)
                recorderBus.emit(buffer)
                stream.write(mp3Buffer, 0, encResult)
            }
            events.sendFinishLog(outputFile, elapsed.get())
            Result.success(Unit)

        } catch (error: Error) {
            Result.failure(error)
        } finally {
            stream.close()
            recorder.stop()
            LameModule.close()
            recorder.release()
        }
    }

    private suspend fun initRecorder(
        sampleRate: Int,
        bitRate: Int,
        audioRecord: AudioRecord
    ): Result<AudioRecord> {
        try {
            LameModule.init(sampleRate, 2, sampleRate, bitRate, quality)
        } catch (e: Exception) {
            return Result.failure(Error("Error initializing recorder"))
        }
        return if (audioRecord.state == AudioRecord.STATE_INITIALIZED) {
            val logMessage = Stringer(
                R.string.rcdr_recording_mp3_at,
                bitRate,
                sampleRate
            )
            events.emit(Mp3VoiceRecorder.Event.Message(logMessage))
            Result.success(audioRecord)
        } else {
            Result.failure(Error("Error initializing recorder"))
        }
    }

    internal companion object {
        const val MIN_BUFFER_SIZE_INDEX = 10
        val channelConfig: Int = Mp3VoiceRecorder.CHANNEL_PRESETS[0]
        val quality: Int = Mp3VoiceRecorder.QUALITY_PRESETS[1]
        val encoding: Int = Mp3VoiceRecorder.AUDIO_FORMAT_PRESETS[1]
    }
}

