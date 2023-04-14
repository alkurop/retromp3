package com.omar.retromp3recorder.app.screens.home.components.menu.popups.rename

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.files.CanRenameNameUC
import com.omar.retromp3recorder.bl.files.RenameFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.repo.PublishSubjectRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RenameFileInteractor @Inject constructor(
    private val canRenameNameUC: CanRenameNameUC,
    private val currentFileRepo: CurrentFileRepo,
    private val renameFileUC: RenameFileUC,
    dispatcher: CoroutineDispatcher
) : Interactor<RenameFileContract.Input, RenameFileContract.Output>(dispatcher) {
    private val outputBus = PublishSubjectRepo<RenameFileContract.Output>()

    override fun listRepos(): List<Flow<RenameFileContract.Output>> {
        return listOf(
            outputBus,
            currentFileRepo.flow()
                .map {
                    RenameFileContract.Output.CurrentFile(requireNotNull(it.value as? ExistingFileWrapper) {
                        "current file is not ExistingFileWrapper but ${it.value}"
                    })
                },
        )
    }

    override suspend fun launchUseCase(input: RenameFileContract.Input) {
        when (input) {
            is RenameFileContract.Input.Rename -> {
                renameFileUC.execute(input.newName)
                outputBus.emit(RenameFileContract.Output.Dismiss)
            }
            is RenameFileContract.Input.CheckCanRename -> {
                val canRename = canRenameNameUC.execute(
                    input.newName,
                )
                outputBus.emit(RenameFileContract.Output.OkButtonState(canRename, input.newName))
            }
        }
    }
}

