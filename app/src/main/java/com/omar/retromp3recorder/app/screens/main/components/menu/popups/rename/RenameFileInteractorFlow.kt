package com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename

import com.omar.retromp3recorder.bl.files.CanRenameNameUC
import com.omar.retromp3recorder.bl.files.RenameFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RenameFileInteractorFlow @Inject constructor(
    private val canRenameNameUC: CanRenameNameUC,
    private val currentFileRepo: CurrentFileRepo,
    private val renameFileUC: RenameFileUC,
    private val dispatcher: CoroutineDispatcher
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + dispatcher
    private val shouldDismiss = MutableSharedFlow<Boolean>()

    fun processIO(upstream: Flow<RenameFileContract.Input>): Flow<RenameFileContract.Output> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().flowOn(dispatcher)
    }


    private fun Flow<RenameFileContract.Input>.processInputs(): Flow<RenameFileContract.Output> {
        return this.transform { input ->

            when (input) {
                is RenameFileContract.Input.Rename -> {
                    launch {
                        renameFileUC.execute(input.newName)
                        shouldDismiss.emit(true)
                    }
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

    private fun listenToRepos(): Flow<RenameFileContract.Output> {
        return listOf(
            shouldDismiss.map { RenameFileContract.Output.Dismiss },
            currentFileRepo.flow()
                .map { RenameFileContract.Output.CurrentFile(it.value as ExistingFileWrapper) },
        ).merge()
    }
}

