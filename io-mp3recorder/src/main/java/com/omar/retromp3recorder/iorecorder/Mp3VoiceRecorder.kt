package com.omar.retromp3recorder.iorecorder

import android.media.AudioFormat
import android.media.projection.MediaProjection
import androidx.annotation.StringRes
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.iorecorder.R
import io.reactivex.rxjava3.core.Observable

interface Mp3VoiceRecorder {
    fun observeEvents(): Observable<Event>
    fun recordWithProps(props: RecorderProps)
    fun stopRecord()
    fun isRecording(): Boolean

    enum class SampleRate(val value: Int) {
        _44100(44100), _22050(22050), _11025(11025), _8000(8000);
    }

    enum class BitRate(val value: Int) {
        _320(320), _192(192), _160(160), _128(128);
    }

    enum class AudioSourcePref(
        @StringRes val title: Int
    ) {
        Mic(title = R.string.rcdr_mic),
        Media(title = R.string.rcdr_media_output),
        Call(title = R.string.rcdr_call),
    }

    sealed class AudioSource {
        object Mic : AudioSource()
        class Output(val mediaProjection: MediaProjection, val source: Int) : AudioSource()
    }

    sealed class Event {
        data class Message(val message: Stringer) : Event()
        data class Error(val error: Stringer) : Event()
    }

    data class RecorderProps(
        val filepath: String,
        val bitRate: BitRate,
        val sampleRate: SampleRate,
        val audioSourcePref: AudioSource
    )

    companion object {
        val AUDIO_FORMAT_PRESETS = intArrayOf(
            AudioFormat.ENCODING_PCM_8BIT,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val QUALITY_PRESETS = intArrayOf(0, 2, 5) // the lower the better
        val CHANNEL_PRESETS = intArrayOf(
            AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO
        )
    }

    fun observeState(): Observable<State>

    enum class State {
        Idle,
        Recording
    }

    fun observeRecorder(): Observable<ByteArray>
}
