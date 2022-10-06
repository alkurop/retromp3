package com.omar.retromp3recorder.app.ui.menu.popups.crop.logic

import com.omar.retromp3recorder.app.ui.menu.popups.crop.CropContract
import com.omar.retromp3recorder.bl.files.CanSaveAsName
import com.omar.retromp3recorder.dto.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.storage.repo.local.MenuPopupBus
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.mapToUsecase
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class CropInteractor @Inject constructor(
    private val canSaveAs: CanSaveAsName,
    private val cropInPlaceUC: CropInPlaceUC,
    private val cropOutsideUC: CropOutsideUC,
    private val nameUpdater: CropFileNameUpdater,
    private val popupBus: MenuPopupBus,
    private val scheduler: Scheduler
) {
    private val canCropFileRepo = BehaviorSubjectRepo(false)
    private val nameSuggestionRepo = BehaviorSubjectRepo(NewNameSuggestion())

    fun processIO(): ObservableTransformer<CropContract.Input, CropContract.Output> =
        scheduler.processIO(
            inputMapper = marInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<CropContract.Output> = {
        Observable.merge(
            listOf(
                canCropFileRepo.observe().map {
                    CropContract.Output.IsActionEnabled(it)
                },
                nameUpdater.observe().map {
                    val nameSuggestion =
                        it.value?.apply { canCropFileRepo.onNext(true) } ?: NewNameSuggestion()
                    nameSuggestionRepo.onNext(nameSuggestion)
                    CropContract.Output.FileNameUpdate(nameSuggestion)
                },
                nameSuggestionRepo.observe().map {
                    CropContract.Output.FileNameUpdate(it)
                }
            )
        )
    }

    private val marInputToUsecase: (Observable<CropContract.Input>) -> Completable = { input ->
        Completable.merge(
            listOf(
                input.mapToUsecase<CropContract.Input.DismissPopup> {
                    Completable.fromAction { popupBus.onNext(Optional.empty()) }
                },
                input.mapToUsecase<CropContract.Input.CheckCanCrop> { action ->
                    Completable.fromAction { nameSuggestionRepo.onNext(action.nameSuggestion) }
                        .andThen(
                            canSaveAs.execute(
                                action.nameSuggestion.path,
                                canCropFileRepo
                            )
                        )
                },
                input.mapToUsecase<CropContract.Input.CropInPlace> {
                    cropInPlaceUC.execute(it.nameSuggestion)
                },
                input.mapToUsecase<CropContract.Input.CropOutside> {
                    cropOutsideUC.execute(it.nameSuggestion)
                        .flatMapCompletable { Completable.complete() }
                }
            )
        )
    }
}
