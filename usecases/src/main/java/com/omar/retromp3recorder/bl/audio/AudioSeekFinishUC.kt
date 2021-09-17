package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AudioSeekFinishUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val playerProgressRepo: PlayerProgressRepo
) {
    fun execute(): Completable =
        Observable.combineLatest(
            audioPlayer.observeState().takeOne(),
            playerProgressRepo.observe().takeOne(),
            { state, progress -> Pair(state, progress) })
            .flatMapCompletable { (state, progress) ->
                Completable.fromAction {
                    val position = progress.value!!.progress

                    when (state) {
                        AudioPlayer.State.Playing -> {
                            error("Should not be seeking during $state")
                        }
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
