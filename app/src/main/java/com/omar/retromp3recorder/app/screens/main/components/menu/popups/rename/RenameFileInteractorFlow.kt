package com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename

import com.omar.retromp3recorder.bl.files.CanRenameNameUC
import com.omar.retromp3recorder.bl.files.RenameFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class RenameFileInteractorFlow @Inject constructor(
    private val canRenameNameUC: CanRenameNameUC,
    private val currentFileRepo: CurrentFileRepo,
    private val renameFileUC: RenameFileUC,
    private val dispatcher: CoroutineDispatcher
) {
    private val shouldDismiss = MutableSharedFlow<Boolean>()

    fun processIO(upstream: Flow<RenameFileContract.Input>): Flow<RenameFileContract.Output> {
        return listOf(
            listenToRepos(),
            upstream.processInputs(),
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<RenameFileContract.Input>.processInputs(): Flow<RenameFileContract.Output> {
        return this.flatMapMerge { input ->
            flow {
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
    }

    private fun listenToRepos(): Flow<RenameFileContract.Output> {
        return listOf(
            shouldDismiss.map { RenameFileContract.Output.Dismiss },
            currentFileRepo.flow()
                .map {
                    RenameFileContract.Output.CurrentFile(requireNotNull(it.value as? ExistingFileWrapper) {
                        "current file is not ExistingFileWrapper but ${it.value}"
                    })
                },
        ).merge()
    }
}

