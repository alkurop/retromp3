package com.omar.retromp3recorder.app.ui.files.rename

import com.omar.retromp3recorder.bl.files.CanRenameName
import com.omar.retromp3recorder.bl.files.RenameFileUC
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.mapToUsecase
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class RenameFileInteractor @Inject constructor(
    private val canRenameNameUC: CanRenameName,
    private val currentFileRepo: CurrentFileRepo,
    private val renameFileUC: RenameFileUC,
    private val scheduler: Scheduler
) {
    private val canRenameFileRepo = BehaviorSubjectRepo(false)
    private val finishedRenameCallback = BehaviorSubjectRepo(false)

    fun processIO(): ObservableTransformer<RenameFileView.Input, RenameFileView.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<RenameFileView.Output> = {
        Observable.merge(
            listOf(
                currentFileRepo.observe().map { RenameFileView.Output.CurrentFile(it.value as ExistingFileWrapper) },
                canRenameFileRepo.observe().map { RenameFileView.Output.OkButtonState(it) },
                finishedRenameCallback.observe().map { RenameFileView.Output.CanDismiss(it) }
            )
        )
    }
    private val mapInputToUsecase: (Observable<RenameFileView.Input>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.mapToUsecase<RenameFileView.Input.Rename> {
                        renameFileUC.execute(it.newName)
                            .andThen {
                                finishedRenameCallback.onNext(true)
                            }
                    },
                    input.mapToUsecase<RenameFileView.Input.CheckCanRename> {
                        canRenameNameUC.execute(
                            it.newName,
                            canRenameFileRepo = canRenameFileRepo
                        )
                    }
                )
            )
        }
}

