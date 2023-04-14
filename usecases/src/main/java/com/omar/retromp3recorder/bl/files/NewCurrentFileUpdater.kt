package com.omar.retromp3recorder.bl.files

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.domain.PlayerProgress
import com.omar.retromp3recorder.domain.PlayerRange
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import com.omar.retromp3recorder.storage.repo.local.RangeBarResetBus
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Cleans SeekPositionRepo when current file changes
 */
class NewCurrentFileUpdater @Inject constructor(
    private val hasPlayableFileMapper: HasPlayableFileMapper,
    private val playerProgressRepo: PlayerProgressRepo,
    private val rangeBarResetBus: RangeBarResetBus,
    private val jobWrapper: AudioCoroutineContext
) {
    suspend fun execute() {
        jobWrapper.launch {
            hasPlayableFileMapper.flow()
                .collect { currentFile ->
                    val unwrappedFile = currentFile.value
                    val updatedProgress = if (unwrappedFile != null) {
                        PlayerProgressRepo.In.NewCurrentFile(
                            PlayerProgress(
                                0,
                                requireNotNull(unwrappedFile.length) {
                                    "Fle length should not be null"
                                },
                                PlayerRange()
                            )
                        )
                    } else PlayerProgressRepo.In.Hidden

                    playerProgressRepo.emit(updatedProgress)
                    rangeBarResetBus.emit(Shell(0))
                }
        }
    }
}
