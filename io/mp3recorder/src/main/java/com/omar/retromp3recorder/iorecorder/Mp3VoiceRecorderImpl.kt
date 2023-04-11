package com.omar.retromp3recorder.iorecorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioPlaybackCaptureConfiguration
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.os.Build
import android.os.Process
import androidx.annotation.RequiresApi
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.recorder.LameModule
import com.omar.retromp3recorder.io.recorder.R
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder.Companion.AUDIO_FORMAT_PRESETS
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder.Companion.CHANNEL_PRESETS
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder.Companion.QUALITY_PRESETS
import com.omar.retromp3recorder.iorecorder.RecorderObserver.sendFinishLog
import com.omar.retromp3recorder.utils.platform.disposedBy
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.asFlow
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

class Mp3VoiceRecorderImpl @Inject internal constructor(
    private val scheduler: Scheduler,
    @ApplicationContext private val context: Context
) : Mp3VoiceRecorder {
    private val events: Subject<Mp3VoiceRecorder.Event> = PublishSubject.create()
    private val elapsed = AtomicLong(0)
    private val compositeDisposable = CompositeDisposable()
    private val recorderBus = PublishSubject.create<ShortArray>()
    private val state = BehaviorSubject.createDefault(Mp3VoiceRecorder.State.Idle)

    override fun stateFlow(): Flow<Mp3VoiceRecorder.State> {
        return state.asFlow()
    }

    override fun recorderFlow(): Flow<ByteArray> {
        return RecorderObserver.map(recorderBus).asFlow()
    }

    override fun eventsFlow(): Flow<Mp3VoiceRecorder.Event> = events.asFlow()

    override fun recordWithProps(props: Mp3VoiceRecorder.RecorderProps) {
        val sampleRate = props.prefs.sampleRate.value
        val bitRate = props.prefs.bitRate.value
        val minBufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            channelConfig,
            encoding
        )
        val findAudioRecord = when (val audioSource = props.audioSourcePref) {
            Mp3VoiceRecorder.AudioSource.Mic -> findAudioRecordForMic(
                minBufferSize = minBufferSize,
                sampleRate = sampleRate
            )
            is Mp3VoiceRecorder.AudioSource.Output -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                findAudioRecordForMediaProjection(
                    audioSource.mediaProjection,
                    audioSource.source,
                    minBufferSize,
                    sampleRate = sampleRate
                )
            else findAudioRecordForMic(
                minBufferSize = minBufferSize,
                sampleRate = sampleRate
            )
        }

        Single
            .zip(
                createOutputFile(props.filepath),
                findAudioRecord
            ) { file, audioRecord -> Pair(file, audioRecord) }

            .flatMap { (file, audioRecord) ->
                createRecorder(
                    audioRecord = audioRecord,
                    sampleRate = sampleRate,
                    bitRate = bitRate
                ).map { processedAudioRecord -> Pair(file, processedAudioRecord) }
            }
            .flatMapCompletable { fileAudioRecordPair ->
                record(
                    outputFile = fileAudioRecordPair.first,
                    recorder = fileAudioRecordPair.second,
                    sampleRate = sampleRate
                )
            }
            .onErrorResumeNext { throwable ->
                events.onNext(
                    Mp3VoiceRecorder.Event.Error(
                        Stringer.ofString(throwable.message ?: throwable.toString())
                    )
                )
                Timber.e(throwable)
                Completable.complete()
            }
            .subscribeOn(scheduler)
            .doOnSubscribe { state.onNext(Mp3VoiceRecorder.State.Recording) }
            .doFinally { state.onNext(Mp3VoiceRecorder.State.Idle) }
            .subscribe()
            .disposedBy(compositeDisposable)
    }

    override fun stopRecord() {
        compositeDisposable.clear()
        state.onNext(Mp3VoiceRecorder.State.Idle)
    }

    private fun createMp3Buffer(buffer: ShortArray): ByteArray {
        val mp3BufferSize = 7200 + buffer.size * 1.25
        return ByteArray(mp3BufferSize.toInt())
    }

    private fun createRecorder(
        audioRecord: AudioRecord,
        sampleRate: Int,
        bitRate: Int
    ): Single<AudioRecord> = Single.fromCallable {
        initRecorder(sampleRate = sampleRate, bitRate = bitRate, audioRecord = audioRecord)
    }

    @SuppressLint("MissingPermission")
    @Throws(Exception::class)
    private fun findAudioRecordForMic(
        minBufferSize: Int,
        sampleRate: Int,
    ): Single<AudioRecord> {
        return Single.fromCallable {
            if (minBufferSize != AudioRecord.ERROR_BAD_VALUE) {
                AudioRecord(
                    MediaRecorder.AudioSource.DEFAULT,
                    sampleRate,
                    channelConfig,
                    encoding,
                    minBufferSize * MIN_BUFFER_SIZE_INDEX
                )
            } else throw Exception(context.getString(R.string.rcdr_audio_record_bad_value))
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingPermission")
    private fun findAudioRecordForMediaProjection(
        mediaProjection: MediaProjection,
        source: Int,
        minBufferSize: Int,
        sampleRate: Int,
    ): Single<AudioRecord> = Single.fromCallable {
        val config = AudioPlaybackCaptureConfiguration.Builder(mediaProjection)
            .addMatchingUsage(source)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setEncoding(encoding)
            .setSampleRate(sampleRate)
            .setChannelMask(channelConfig)
            .build()

        AudioRecord.Builder()
            .setBufferSizeInBytes(minBufferSize * MIN_BUFFER_SIZE_INDEX)
            .setAudioFormat(audioFormat)
            .setAudioPlaybackCaptureConfig(config)
            .build()
    }

    private fun createOutputFile(filePath: String): Single<File> {
        return Single.fromCallable {
            val outFile = File(filePath)
            if (outFile.exists()) {
                outFile.delete()
            }
            val fileWasCreated = outFile.createNewFile()
            if (!fileWasCreated) {
                throw IOException(
                    context.getString(R.string.rcdr_file_was_not_created, filePath)
                )
            }
            outFile
        }
    }

    private fun record(outputFile: File, recorder: AudioRecord, sampleRate: Int): Completable {
        return Completable.create { emitter: CompletableEmitter ->
            try {
                elapsed.set(System.currentTimeMillis())
                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
                val output = FileOutputStream(outputFile)
                emitter.setCancellable {
                    events.sendFinishLog(outputFile, elapsed.get())
                    recorder.stop()
                    LameModule.close()
                    recorder.release()
                    output.close()
                }
                val buffer = ShortArray(sampleRate)
                val mp3Buffer = createMp3Buffer(buffer)
                val minBufferSize = AudioRecord
                    .getMinBufferSize(sampleRate, channelConfig, encoding)
                recorder.startRecording()
                var readSize: Int
                while (!emitter.isDisposed) {
                    readSize = recorder.read(buffer, 0, minBufferSize)
                    //left and right channel goes into one
                    val encResult = LameModule.encode(buffer, buffer, readSize, mp3Buffer)
                    recorderBus.onNext(buffer)
                    output.write(mp3Buffer, 0, encResult)
                }
            } catch (e: Throwable) {
                emitter.tryOnError(e)
            }
        }
    }

    private fun initRecorder(
        sampleRate: Int,
        bitRate: Int,
        audioRecord: AudioRecord
    ): AudioRecord {
        try {
            LameModule.init(sampleRate, 2, sampleRate, bitRate, quality)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.rcdr_error_init_recorder))
        }
        if (audioRecord.state == AudioRecord.STATE_INITIALIZED) {
            val logMessage = Stringer(
                R.string.rcdr_recording_mp3_at,
                bitRate,
                sampleRate
            )
            events.onNext(Mp3VoiceRecorder.Event.Message(logMessage))
        } else throw Exception(context.getString(R.string.rcdr_error_init_recorder))
        return audioRecord
    }
}

private const val MIN_BUFFER_SIZE_INDEX = 10
private val channelConfig: Int = CHANNEL_PRESETS[0]
private val quality: Int = QUALITY_PRESETS[1]
private val encoding: Int = AUDIO_FORMAT_PRESETS[1]
