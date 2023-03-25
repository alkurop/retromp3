package com.omar.retromp3recorder.audioplayer

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.github.alkurop.stringerbell.Stringer
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.STATE_ENDED
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.rx3.asFlow
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AudioPlayerExoImpl @Inject constructor(
    @ApplicationContext val context: Context
) : AudioPlayer {
    private val events = PublishSubject.create<AudioPlayer.Output.Event>()
    private val state = BehaviorSubject.createDefault(AudioPlayer.State.Idle)
    private val progress = BehaviorSubject.create<AudioPlayer.Output.Progress>()
    private val mediaPlayer: ExoPlayer = SimpleExoPlayer.Builder(context).build()
    private val handler = Handler(Looper.getMainLooper())
    private val compositeDisposable = CompositeDisposable()
    private lateinit var options: PlayerStartOptions

    override fun flow(): Flow<AudioPlayer.Output> {
        return Observable.merge(
            progress.distinctUntilChanged()
                .map {
                    val range = options.rangeMillis
                    if (it.end) {
                        val position = if (options.isStopToRangeStartEnabled) range.from else 0
                        it.copy(
                            position = position,
                            duration = options.length
                        )
                    } else {
                        it.copy(position = it.position + range.from, duration = options.length)
                    }
                },
            events
        ).asFlow()
    }

    override fun stateFlow(): Flow<AudioPlayer.State> {
        return state.asFlow()
    }

    override fun onInput(input: AudioPlayer.Input) {
        compositeDisposable.clear()
        handler.post {
            when (input) {
                is AudioPlayer.Input.SeekPause -> {
                    mediaPlayer.stop()
                    events.onNext(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_seek_pause)))
                    state.onNext(AudioPlayer.State.PausedToSeek)
                }

                is AudioPlayer.Input.Stop -> {
                    stopMedia()
                }
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
            events.onNext(AudioPlayer.Output.Event.Error(Stringer(R.string.aplr_player_cannot_find_file)))
            return
        }
        mediaPlayer
            .apply {

                val (from, to) = options.rangeMillis
                val uri: Uri = Uri.fromFile(File(options.filePath))

                val mediaItem: MediaItem = MediaItem.Builder()
                    .setUri(uri)
                    .setClipStartPositionMs(from)
                    .setClipEndPositionMs(to)
                    .build()

                setMediaItem(mediaItem)
                seekTo(options.relativeSeekPosition)
                playWhenReady = true
                state.onNext(AudioPlayer.State.Playing)
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == STATE_ENDED) {
                            progress.onNext(
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
                                events.onNext(AudioPlayer.Output.Event.AudioSessionId(it))
                            }
                            events.onNext(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_started_playing)))
                        }
                    }

                    override fun onPlayerError(error: ExoPlaybackException) {
                        events.onNext(AudioPlayer.Output.Event.Error(Stringer.ofString(error.toString())))
                    }
                })
                prepare()
            }
    }

    private fun stopMedia() {
        compositeDisposable.clear()
        mediaPlayer.apply {
            stop()
        }
        state.onNext(AudioPlayer.State.Idle)
        events.onNext(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_stopped_playing)))
    }

    private fun initProgressUpdate() {
        compositeDisposable.clear()
        compositeDisposable += Observable.interval(10, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val position = mediaPlayer.currentPosition
                val duration = (options.rangeMillis.length)
                progress.onNext(AudioPlayer.Output.Progress(position, duration, false))
            }
    }
}
