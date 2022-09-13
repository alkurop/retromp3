package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AudioSeekFinishUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val playerProgressRepo: PlayerProgressRepo
) {
    fun execute(): Completable =
        Single.zip(
            audioPlayer.observeState().takeOne(),
            playerProgressRepo.observe().takeOne()
        ) { state, progress -> Pair(state, progress) }
            .flatMapCompletable { (state, progress) ->
                Completable.fromAction {
                    val position = progress.value!!.progress
                    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                    when (state) {
                        AudioPlayer.State.Playing ,
                        AudioPlayer.State.Seek_Paused -> {
                            audioPlayer.onInput(AudioPlayer.Input.Seek(position))
                            audioPlayer.onInput(AudioPlayer.Input.Resume)
                        }
                        AudioPlayer.State.Idle -> {
                            //do nothing
                        }
                    }
                }
            }
}
