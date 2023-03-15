package com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete

import com.omar.retromp3recorder.bl.files.DeleteCurrentFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DeleteFileInteractorFlow @Inject constructor(
    private val deleteCurrentFileUC: DeleteCurrentFileUC,
    private val currentFileMapper: CurrentFileRepo,
    private val dispatcher: CoroutineDispatcher
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + dispatcher
    private val shouldDismiss = MutableSharedFlow<Boolean>()

    fun processIO(upstream: Flow<DeleteFileContract.Input>): Flow<DeleteFileContract.Output> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<DeleteFileContract.Input>.processInputs(): Flow<DeleteFileContract.Output> {
        return this.transform { input ->
            when (input) {
                is DeleteFileContract.Input.DeleteFile -> {
                    launch {
                        deleteCurrentFileUC.execute()
                        shouldDismiss.emit(false)
                    }
                }
            }
        }
    }

    private fun listenToRepos(): Flow<DeleteFileContract.Output> =
        listOf(
            shouldDismiss.map { DeleteFileContract.Output.Dismiss },
            currentFileMapper.flow().map {
                DeleteFileContract.Output.CurrentFile(
                    it.value as? ExistingFileWrapper
                )
            },
        ).merge()
}


