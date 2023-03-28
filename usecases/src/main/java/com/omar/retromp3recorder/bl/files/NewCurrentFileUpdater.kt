package com.omar.retromp3recorder.bl.files

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.bl.enablers.DeactivatePlayerControlsUC
import com.omar.retromp3recorder.domain.PlayerProgress
import com.omar.retromp3recorder.domain.PlayerRange
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import com.omar.retromp3recorder.storage.repo.local.RangeBarResetBus
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Cleans SeekPositionRepo when current file changes
 */
class NewCurrentFileUpdater @Inject constructor(
    private val hasPlayableFileMapper: HasPlayableFileMapper,
    private val playerProgressRepo: PlayerProgressRepo,
    private val rangeBarResetBus: RangeBarResetBus,
    private val deactivatePlayerControlsUC: DeactivatePlayerControlsUC,
    private val jobWrapper: ScopeJobWrapper
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
                    deactivatePlayerControlsUC.execute()
                }
        }
    }
}
