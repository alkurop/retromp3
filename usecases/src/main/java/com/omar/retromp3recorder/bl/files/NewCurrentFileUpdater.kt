package com.omar.retromp3recorder.bl.files

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.PlayerProgress
import com.omar.retromp3recorder.dto.PlayerRange
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import com.omar.retromp3recorder.storage.repo.local.RangeBarResetBus
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

/**
 * Cleans SeekPositionRepo when current file changes
 */
class NewCurrentFileUpdater @Inject constructor(
    private val hasPlayableFileMapper: HasPlayableFileMapper,
    private val playerProgressRepo: PlayerProgressRepo,
    private val rangeBarResetBus: RangeBarResetBus,
    private val scheduler: Scheduler
) {
    fun execute(): Completable = hasPlayableFileMapper.observe()
        .map { currentFile ->
            val unwrappedFile = currentFile.value
            if (unwrappedFile != null) {
                PlayerProgressRepo.In.NewCurrentFile(
                    PlayerProgress(
                        0,
                        unwrappedFile.length!!,
                        PlayerRange()
                    )
                )
            } else PlayerProgressRepo.In.Hidden
        }
        .flatMapCompletable { progress ->
            Completable.fromAction {
                playerProgressRepo.onNext(progress)
                rangeBarResetBus.onNext(Shell(0))
            }
        }
        .subscribeOn(scheduler)
}
