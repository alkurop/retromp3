package com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename

import com.omar.retromp3recorder.bl.files.CanRenameName
import com.omar.retromp3recorder.bl.files.RenameFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.common.StateFlowRepo
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RenameFileInteractorFlow @Inject constructor(
    private val canRenameNameUC: CanRenameName,
    private val currentFileRepo: CurrentFileRepo,
    private val renameFileUC: RenameFileUC,
    dispatcher: CoroutineDispatcher
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + dispatcher

    private val canRenameFileRepo = StateFlowRepo(Pair<Boolean, String?>(false, null))
    private val shouldDismiss = PublishSubject.create<Boolean>()

    fun processIO(upstream: Flow<RenameFileContract.Input>): Flow<RenameFileContract.Output> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().distinctUntilChanged()
    }


    private fun Flow<RenameFileContract.Input>.processInputs(): Flow<RenameFileContract.Output> {
        return this.transform { input ->
            launch {
                when (input) {
                    is RenameFileContract.Input.Rename ->{
                        renameFileUC.execute(input.newName).andThen(Completable.fromAction {
                            shouldDismiss.onNext(true)
                        }).blockingAwait()
                    }
                    is RenameFileContract.Input.CheckCanRename-> {
                        canRenameNameUC.execute(
                            input.newName,
                            canRenameFileRepo = canRenameFileRepo
                        ).blockingAwait()
                    }
                }
            }
        }
    }

    private fun listenToRepos(): Flow<RenameFileContract.Output> {
        return listOf(
            shouldDismiss.asFlow().map { RenameFileContract.Output.Dismiss },
            currentFileRepo.flow()
                .map { RenameFileContract.Output.CurrentFile(it.value as ExistingFileWrapper) },
            canRenameFileRepo.flow()
                .map { RenameFileContract.Output.OkButtonState(it.first, it.second) },
        ).merge()
    }
}

