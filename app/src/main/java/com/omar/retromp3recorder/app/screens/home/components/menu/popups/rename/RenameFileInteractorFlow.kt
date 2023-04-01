package com.omar.retromp3recorder.app.screens.home.components.menu.popups.rename

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.files.CanRenameNameUC
import com.omar.retromp3recorder.bl.files.RenameFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RenameFileInteractorFlow @Inject constructor(
    private val canRenameNameUC: CanRenameNameUC,
    private val currentFileRepo: CurrentFileRepo,
    private val renameFileUC: RenameFileUC,
    dispatcher: CoroutineDispatcher
) : Interactor<RenameFileContract.Input, RenameFileContract.Output>(dispatcher) {
    private val shouldDismiss = MutableSharedFlow<Boolean>()

    override fun listRepos(): List<Flow<RenameFileContract.Output>> {
        return listOf(
            shouldDismiss.map { RenameFileContract.Output.Dismiss },
            currentFileRepo.flow()
                .map {
                    RenameFileContract.Output.CurrentFile(requireNotNull(it.value as? ExistingFileWrapper) {
                        "current file is not ExistingFileWrapper but ${it.value}"
                    })
                },
        )
    }

    override suspend fun FlowCollector<RenameFileContract.Output>.launchUseCase(input: RenameFileContract.Input) {
        when (input) {
            is RenameFileContract.Input.Rename -> {
                renameFileUC.execute(input.newName)
                shouldDismiss.emit(true)
            }
            is RenameFileContract.Input.CheckCanRename -> {
                val canRename = canRenameNameUC.execute(
                    input.newName,
                )
                emit(RenameFileContract.Output.OkButtonState(canRename, input.newName))
            }
        }
    }
}

