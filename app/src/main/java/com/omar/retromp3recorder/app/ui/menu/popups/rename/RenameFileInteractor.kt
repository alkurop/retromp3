package com.omar.retromp3recorder.app.ui.menu.popups.rename

import com.omar.retromp3recorder.bl.files.CanRenameName
import com.omar.retromp3recorder.bl.files.RenameFileUC
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.local.MenuPopupBus
import com.omar.retromp3recorder.utils.Optional
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
    private val popupBus: MenuPopupBus,
    private val scheduler: Scheduler
) {
    private val canRenameFileRepo = BehaviorSubjectRepo(Pair<Boolean, String?>(false, null))

    fun processIO(): ObservableTransformer<RenameFileContract.Input, RenameFileContract.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<RenameFileContract.Output> = {
        Observable.merge(
            listOf(
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
                        renameFileUC.execute(it.newName)
                            .andThen {
                                popupBus.onNext(Optional.empty())
                            }
                    },
                    input.mapToUsecase<RenameFileContract.Input.DismissPopup> {
                        Completable.fromAction { popupBus.onNext(Optional.empty()) }
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

