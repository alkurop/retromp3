package com.omar.retromp3recorder.iorecorder

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.iorecorder.R
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject
import java.io.File

object RecorderObserver {
    // todo why 1024?
    fun map(recorder: Observable<ShortArray>): Observable<ByteArray> {
        return recorder
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

    fun Subject<Mp3VoiceRecorder.Event>.sendFinishLog(outFile: File?, elapsed:Long) {
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

