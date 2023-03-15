package com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete

import com.omar.retromp3recorder.bl.files.DeleteCurrentFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class DeleteFileInteractorFlow @Inject constructor(
    private val deleteCurrentFileUC: DeleteCurrentFileUC,
    private val currentFileMapper: CurrentFileRepo,
    private val dispatcher: CoroutineDispatcher
) {
    private val shouldDismiss = MutableSharedFlow<Boolean>()

    fun processIO(upstream: Flow<DeleteFileContract.Input>): Flow<DeleteFileContract.Output> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<DeleteFileContract.Input>.processInputs(): Flow<DeleteFileContract.Output> {
        return this.flatMapMerge { input ->
            channelFlow {
                when (input) {
                    is DeleteFileContract.Input.DeleteFile -> {
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


