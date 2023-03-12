package com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename

import com.omar.retromp3recorder.bl.files.CanRenameName
import com.omar.retromp3recorder.bl.files.RenameFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.common.StateFlowRepo
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.mapToUsecase
import com.omar.retromp3recorder.utils.domain.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class RenameFileInteractor @Inject constructor(
    private val canRenameNameUC: CanRenameName,
    private val currentFileRepo: CurrentFileRepo,
    private val renameFileUC: RenameFileUC,
    private val scheduler: Scheduler
) {
    private val canRenameFileRepo = StateFlowRepo(Pair<Boolean, String?>(false, null))
    private val shouldDismiss = PublishSubject.create<Boolean>()

    fun processIO(): ObservableTransformer<RenameFileContract.Input, RenameFileContract.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<RenameFileContract.Output> = {
        Observable.merge(
            listOf(
                shouldDismiss.map { RenameFileContract.Output.Dismiss },

                currentFileRepo.observe()
                    .map { RenameFileContract.Output.CurrentFile(it.value as ExistingFileWrapper) },
                canRenameFileRepo.observe()
                    .map { RenameFileContract.Output.OkButtonState(it.first, it.second) },
            )
        )
    }
    private val mapInputToUsecase: (Observable<RenameFileContract.Input>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.mapToUsecase<RenameFileContract.Input.Rename> {
                        renameFileUC.execute(it.newName).andThen(Completable.fromAction {
                            shouldDismiss.onNext(true)
                        })
                    },
                    input.mapToUsecase<RenameFileContract.Input.CheckCanRename> {
                        canRenameNameUC.execute(
                            it.newName,
                            canRenameFileRepo = canRenameFileRepo
                        )
                    }
                )
            )
        }
}

