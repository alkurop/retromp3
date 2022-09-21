package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class AudioSeekFinishUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val startPlaybackUC: StartPlaybackUC,
) {
    fun execute(): Completable =

        audioPlayer.observeState().takeOne().flatMapCompletable { state ->
            when (state) {
                AudioPlayer.State.Playing,
                AudioPlayer.State.Seek_Paused -> startPlaybackUC.execute()
                AudioPlayer.State.Idle -> Completable.complete()
            }
        }
}
