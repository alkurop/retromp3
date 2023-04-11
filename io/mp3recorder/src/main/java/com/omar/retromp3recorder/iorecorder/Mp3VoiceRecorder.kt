package com.omar.retromp3recorder.iorecorder

import android.media.AudioFormat
import android.media.projection.MediaProjection
import androidx.annotation.StringRes
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.io.recorder.R
import kotlinx.coroutines.flow.Flow

interface Mp3VoiceRecorder {

    fun eventsFlow(): Flow<Event>
    fun stateFlow(): Flow<State>
    fun recorderFlow(): Flow<ByteArray>

    fun recordWithProps(props: RecorderProps)
    fun stopRecord()

    enum class SampleRate(val value: Int) {
        _48000(48000), _44100(44100), _22050(22050), _11025(11025), _8000(8000);
    }

    enum class BitRate(val value: Int) {
        _320(320), _192(192), _160(160), _128(128);
    }

    enum class WaveTableSampleRate(val value: Int) {
        _100(100)
    }

    enum class AudioSourcePref(
        @StringRes val title: Int
    ) {
        Mic(title = R.string.rcdr_mic),
        Media(title = R.string.rcdr_media_output),
        Games(title = R.string.rcdr_games),
    }

    sealed class AudioSource {
        object Mic : AudioSource()
        class Output(val mediaProjection: MediaProjection, val source: Int) : AudioSource()
    }

    data class RecorderPrefs(
        val sampleRate: SampleRate = SampleRate.values()[0],
        val bitRate: BitRate = BitRate.values()[0],
        val audioSourcePref: AudioSourcePref = AudioSourcePref.values()[0]
    )

    sealed class Event {
        data class Message(val message: Stringer) : Event()
        data class Error(val error: Stringer) : Event()
    }

    data class RecorderProps(
        val filepath: String,
        val prefs: RecorderPrefs,
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

    enum class State {
        Idle,
        Recording
    }
}
