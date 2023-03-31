package com.omar.retromp3recorder.iorecorder

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.io.recorder.R
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject
import java.io.File

object RecorderObserver {
    fun map(recorder: Observable<ShortArray>): Observable<ByteArray> {
        return recorder
            .map { bytes ->
                bytes.toList()
                    .map { ((it * Byte.MAX_VALUE) / (Short.MAX_VALUE)).toByte() }
                    .toByteArray()
            }
    }

    fun Subject<Mp3VoiceRecorder.Event>.sendFinishLog(outFile: File?, elapsed: Long) {
        val counter = System.currentTimeMillis() - elapsed
        val absolutePath = outFile!!.absolutePath
        if (outFile.exists()) {
            val messages = arrayOf(
                Stringer(R.string.rcdr_file_saved_to, absolutePath),
                Stringer(R.string.rcdr_audio_length, counter.toFloat() / MILLIS_IN_SECONDS),
                Stringer(R.string.rcdr_file_size, outFile.length().toFloat() / MILLIS_IN_SECONDS),
                Stringer(R.string.rcdr_compression_rate, outFile.length().toFloat() / counter)
            )
            for (message in messages) {
                onNext(Mp3VoiceRecorder.Event.Message(message))
            }
        } else onNext(
            Mp3VoiceRecorder.Event.Error(
                Stringer(R.string.rcdr_error_saving_file_to, absolutePath)
            )
        )
    }


    private const val ZERO = 0
    private const val ZERO_SHORT = ZERO.toShort()
    private const val MILLIS_IN_SECONDS = 1000
}

