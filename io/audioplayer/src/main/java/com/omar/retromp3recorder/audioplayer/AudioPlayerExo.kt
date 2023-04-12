package com.omar.retromp3recorder.audioplayer

import android.content.Context
import android.net.Uri
import com.github.alkurop.stringerbell.Stringer
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.io.audioplayer.R
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.domain.repo.PublishSubjectRepo
import com.omar.retromp3recorder.utils.platform.tickerFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Named


class AudioPlayerExo @Inject constructor(
    @ApplicationContext val context: Context,
    private val jobWrapper: ScopeJobWrapper,
    @Named("main") private val mainThreadJobWrapper: ScopeJobWrapper
) : AudioPlayer {
    private val events = PublishSubjectRepo<AudioPlayer.Output.Event>(1)
    private val state = MutableStateFlow(AudioPlayer.State.Idle)
    private val progress = PublishSubjectRepo<AudioPlayer.Output.Progress>(1)

    private val mediaPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(context).setLoadControl(DefaultLoadControl()).build()
    }

    private lateinit var options: PlayerStartOptions
    private lateinit var controls: PlayerControls

    override fun flow(): Flow<AudioPlayer.Output> = merge(
        progress.mapWithOptions { options },
        events
    )

    override fun stateFlow(): Flow<AudioPlayer.State> = state

    override fun onInput(input: AudioPlayer.Input) {
        jobWrapper.cancel()
        mainThreadJobWrapper.launch {
            when (input) {
                is AudioPlayer.Input.SeekPause -> {
                    mediaPlayer.stop()
                    events.emit(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_seek_pause)))
                    state.emit(AudioPlayer.State.PausedToSeek)
                }

                is AudioPlayer.Input.Stop -> stopMedia()
                is AudioPlayer.Input.Start -> {
                    setupMediaPlayer(input.options)
                    initProgressUpdate()
                }
                is AudioPlayer.Input.ChangeControls -> {
                    changeControls(input.controls)
                }
            }
        }
    }

    private fun changeControls(controls: PlayerControls) {
        this.controls = controls
        setSpeed()
    }

    private fun setSpeed() {
        if (controls.speedSettings.isEnabled) {
            mediaPlayer.setPlaybackSpeed(controls.speedSettings.speed)
        } else {
            mediaPlayer.setPlaybackSpeed(1f)
        }
    }

    private suspend fun setupMediaPlayer(_options: PlayerStartOptions) {
        this.options = _options
        if (!File(options.filePath).exists()) {
            events.emit(AudioPlayer.Output.Event.Error(Stringer(R.string.aplr_player_cannot_find_file)))
            return
        }
        mediaPlayer.apply {
            val (from, to) = options.rangeMillis
            val uri: Uri = Uri.fromFile(File(options.filePath))

            val mediaItem: MediaItem = MediaItem.Builder().setUri(uri).setClipStartPositionMs(from)
                .setClipEndPositionMs(to).build()

            setSpeed()
            setMediaItem(mediaItem)
            seekTo(options.relativeSeekPosition)
            playWhenReady = true
            state.emit(AudioPlayer.State.Playing)
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == STATE_ENDED) {
                        val length = options.rangeMillis.length
                        progress.tryEmit(AudioPlayer.Output.Progress(length, length, true))
                        stopMedia()
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        audioComponent?.audioSessionId?.let {
                            events.tryEmit(AudioPlayer.Output.Event.AudioSessionId(it))
                        }
                        events.tryEmit(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_started_playing)))
                    }
                }

                override fun onPlayerError(error: ExoPlaybackException) {
                    events.tryEmit(AudioPlayer.Output.Event.Error(Stringer.ofString(error.toString())))
                }
            })
            prepare()
        }
    }

    private fun stopMedia() {
        jobWrapper.cancel()
        mediaPlayer.stop()
        state.tryEmit(AudioPlayer.State.Idle)
        events.tryEmit(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_stopped_playing)))
    }

    private fun initProgressUpdate() {
        jobWrapper.cancel()
        jobWrapper.launch {
            tickerFlow(10).collect {
                mainThreadJobWrapper.launch {
                    val position = mediaPlayer.currentPosition
                    val duration = (options.rangeMillis.length)
                    println(position)
                    progress.tryEmit(AudioPlayer.Output.Progress(position, duration, false))
                }
            }
        }
    }
}
