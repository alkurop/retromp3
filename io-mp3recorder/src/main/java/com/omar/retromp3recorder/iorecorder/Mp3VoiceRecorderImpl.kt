package com.omar.retromp3recorder.iorecorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.*
import android.media.projection.MediaProjection
import android.os.Build
import android.os.Process
import androidx.annotation.RequiresApi
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.iorecorder.R
import com.omar.retromp3recorder.app.recorder.LameModule
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder.Companion.AUDIO_FORMAT_PRESETS
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder.Companion.CHANNEL_PRESETS
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder.Companion.QUALITY_PRESETS
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Mp3VoiceRecorderImpl @Inject internal constructor(
    private val scheduler: Scheduler,
    private val context: Context
) : Mp3VoiceRecorder {
    private val events: Subject<Mp3VoiceRecorder.Event> = PublishSubject.create()
    private val elapsed = AtomicLong(0)
    private val compositeDisposable = CompositeDisposable()
    private val recorderBus = PublishSubject.create<ShortArray>()
    private val state = BehaviorSubject.createDefault(Mp3VoiceRecorder.State.Idle)

    override fun observeState(): Observable<Mp3VoiceRecorder.State> = state
    override fun observeRecorder(): Observable<ByteArray> {
        return recorderBus
            .map { bytes ->
                val target = 1024
                val f = bytes.toList()
                    .filter { it != ZERO_SHORT }
                val bytesInTarget = f.size / target + 1
                val b = f.windowed(bytesInTarget, bytesInTarget, false)
                    .map {
                        (it.average() / (Short.MAX_VALUE / (4 * Byte.MAX_VALUE))).toInt().toShort()
                    }
                b.map { i -> i.toByte() }.toByteArray()
            }
    }

    override fun observeEvents(): Observable<Mp3VoiceRecorder.Event> {
        return events
    }

    override fun recordWithProps(props: Mp3VoiceRecorder.RecorderProps) {
        val minBufferSize = AudioRecord.getMinBufferSize(
            props.sampleRate.value,
            channelConfig,
            encoding
        )
        val findAudioRecord = when (val audioSource = props.audioSourcePref) {
            Mp3VoiceRecorder.AudioSource.Mic -> {
                findAudioRecordForMic(
                    minBufferSize = minBufferSize,
                    sampleRate = props.sampleRate.value
                )
            }
            is Mp3VoiceRecorder.AudioSource.Output -> {
                findAudioRecordForMediaProjection(
                    audioSource.mediaProjection,
                    audioSource.source,
                    minBufferSize,
                    sampleRate = props.sampleRate.value
                )
            }
        }


        Single
            .zip(
                createOutputFile(props.filepath),
                findAudioRecord, { file, audioRecord -> Pair(file, audioRecord) }
            )
            .flatMap { (file, audioRecord) ->
                createRecorder(
                    audioRecord = audioRecord,
                    sampleRate = props.sampleRate.value,
                    bitRate = props.bitRate.value
                ).map { processedAudioRecord -> Pair(file, processedAudioRecord) }
            }
            .flatMapCompletable { fileAudioRecordPair ->
                record(
                    outputFile = fileAudioRecordPair.first,
                    recorder = fileAudioRecordPair.second,
                    sampleRate = props.sampleRate.value
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

    override fun isRecording(): Boolean = state.blockingFirst() == Mp3VoiceRecorder.State.Recording

    override fun stopRecord() {
        compositeDisposable.clear()
        state.onNext(Mp3VoiceRecorder.State.Idle)
    }

    private fun createMp3Buffer(buffer: ShortArray): ByteArray {
        return ByteArray((7200 + buffer.size * 2 * 1.25).toInt())
    }

    private fun createRecorder(
        audioRecord: AudioRecord,
        sampleRate: Int,
        bitRate: Int
    ): Single<AudioRecord> {
        return Single.fromCallable {
            initRecorder(
                sampleRate = sampleRate,
                bitRate = bitRate,
                audioRecord = audioRecord
            )
        }
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
                    context.getString(
                        R.string.rcdr_file_was_not_created,
                        filePath
                    )
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
                    recorder.stop()
                    recorder.release()
                    LameModule.close()
                    output.close()
                    sendFinishLog(outputFile)
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

    private fun sendFinishLog(outFile: File?) {
        val counter = System.currentTimeMillis() - elapsed.get()
        val absolutePath = outFile!!.absolutePath
        if (outFile.exists()) {
            val messages = arrayOf(
                Stringer(R.string.rcdr_file_saved_to, absolutePath),
                Stringer(R.string.rcdr_audio_length, counter.toFloat() / 1000),
                Stringer(R.string.rcdr_file_size, outFile.length().toFloat() / 1000),
                Stringer(R.string.rcdr_compression_rate, outFile.length().toFloat() / counter)
            )
            for (message in messages) {
                events.onNext(Mp3VoiceRecorder.Event.Message(message))
            }
        } else events.onNext(
            Mp3VoiceRecorder.Event.Error(
                Stringer(R.string.rcdr_error_saving_file_to, absolutePath)
            )
        )
    }


    private fun initRecorder(
        sampleRate: Int,
        bitRate: Int,
        audioRecord: AudioRecord
    ): AudioRecord {
        try {
            LameModule.init(sampleRate, 1, sampleRate, bitRate, quality)
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

    companion object {
        private const val ZERO = 0
        private const val ZERO_SHORT = ZERO.toShort()
        private const val MIN_BUFFER_SIZE_INDEX = 10
        private val channelConfig: Int = CHANNEL_PRESETS[0]
        private val quality: Int = QUALITY_PRESETS[1]
        private val encoding: Int = AUDIO_FORMAT_PRESETS[1]
    }
}