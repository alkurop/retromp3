package com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.files.DeleteCurrentFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeleteFileInteractorFlow @Inject constructor(
    private val deleteCurrentFileUC: DeleteCurrentFileUC,
    private val currentFileRepo: CurrentFileRepo,
    dispatcher: CoroutineDispatcher
) : Interactor<DeleteFileContract.Input, DeleteFileContract.Output>(dispatcher) {
    private val shouldDismiss = MutableSharedFlow<Boolean>()

    override fun listRepos(): List<Flow<DeleteFileContract.Output>> {
        return listOf(
            shouldDismiss.map { DeleteFileContract.Output.Dismiss },
            currentFileRepo.flow().map {
                DeleteFileContract.Output.CurrentFile(
                    it.value as? ExistingFileWrapper
                )
            },
        )
    }

    override suspend fun FlowCollector<DeleteFileContract.Output>.launchUseCase(input: DeleteFileContract.Input) {
        when (input) {
            is DeleteFileContract.Input.DeleteFile -> {
                deleteCurrentFileUC.execute()
                shouldDismiss.emit(false)
            }
        }
    }
}


