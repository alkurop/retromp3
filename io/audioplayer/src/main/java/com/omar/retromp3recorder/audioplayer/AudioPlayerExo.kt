package com.omar.retromp3recorder.audioplayer

import android.content.Context
import android.net.Uri
import com.github.alkurop.stringerbell.Stringer
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.omar.retromp3recorder.io.audioplayer.R
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.platform.tickerFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
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
    private val events = MutableSharedFlow<AudioPlayer.Output.Event>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        replay = 1
    )
    private val state = MutableStateFlow(AudioPlayer.State.Idle)
    private val progress = MutableSharedFlow<AudioPlayer.Output.Progress>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        replay = 1
    )

    private val mediaPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(context).setLoadControl(DefaultLoadControl()).build()
    }

    private lateinit var options: PlayerStartOptions

    override fun flow(): Flow<AudioPlayer.Output> {
        return merge(
            progress.map {
                val range = options.rangeMillis
                if (it.end) {
                    val position = if (options.isStopToRangeStartEnabled) range.from else 0
                    it.copy(
                        position = position, duration = options.length
                    )
                } else {
                    it.copy(position = it.position + range.from, duration = options.length)
                }
            },
            events
        )
    }

    override fun stateFlow(): Flow<AudioPlayer.State> {
        return state
    }

    override fun onInput(input: AudioPlayer.Input) {
        jobWrapper.cancel()
        mainThreadJobWrapper.launch {
            when (input) {
                is AudioPlayer.Input.SeekPause -> {
                    mediaPlayer.stop()
                    events.tryEmit(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_seek_pause)))
                    state.tryEmit(AudioPlayer.State.PausedToSeek)
                }

                is AudioPlayer.Input.Stop -> stopMedia()
                is AudioPlayer.Input.Start -> {
                    setupMediaPlayer(input.options)
                    initProgressUpdate()
                }
            }
        }
    }

    private fun setupMediaPlayer(_options: PlayerStartOptions) {
        this.options = _options
        if (!File(options.filePath).exists()) {
            events.tryEmit(AudioPlayer.Output.Event.Error(Stringer(R.string.aplr_player_cannot_find_file)))
            return
        }
        mediaPlayer.apply {
            val (from, to) = options.rangeMillis
            val uri: Uri = Uri.fromFile(File(options.filePath))

            val mediaItem: MediaItem = MediaItem.Builder().setUri(uri).setClipStartPositionMs(from)
                .setClipEndPositionMs(to).build()

            setMediaItem(mediaItem)
            seekTo(options.relativeSeekPosition)
            playWhenReady = true
            state.tryEmit(AudioPlayer.State.Playing)
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == STATE_ENDED) {
                        progress.tryEmit(
                            AudioPlayer.Output.Progress(
                                options.rangeMillis.length,
                                options.rangeMillis.length,
                                true
                            )
                        )
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
