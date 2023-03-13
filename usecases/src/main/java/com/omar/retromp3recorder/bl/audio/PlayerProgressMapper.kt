package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.observeProgress
import com.omar.retromp3recorder.domain.PlayerProgress
import com.omar.retromp3recorder.domain.PlayerRange
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import com.omar.retromp3recorder.utils.domain.toSeekbarTime
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class PlayerProgressMapper @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val playerProgressRepo: PlayerProgressRepo
) {
    fun execute(): Completable =
        audioPlayer.observeProgress()
            .distinctUntilChanged()
            .flatMapCompletable { (position, duration) ->
                Completable.fromAction {
                    val mappedPosition = position.toSeekbarTime()
                    val mappedDuration = duration.toSeekbarTime()
                    val fixedPosition = if (mappedDuration == mappedPosition) 0 else position
                    playerProgressRepo.emit(
                        PlayerProgressRepo.In.Progress(
                            PlayerProgress(
                                fixedPosition,
                                duration,
                                PlayerRange()
                            )
                        )
                    )
                }
            }
}
