package com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.bl.crop.CropFileNameUpdater
import com.omar.retromp3recorder.bl.crop.CropInPlaceUC
import com.omar.retromp3recorder.bl.crop.CropOutsideUC
import com.omar.retromp3recorder.bl.files.CanSaveAsName
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.common.StateFlowRepo
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import com.omar.retromp3recorder.utils.domain.mapToUsecase
import com.omar.retromp3recorder.utils.domain.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class CropInteractor @Inject constructor(
    private val canSaveAs: CanSaveAsName,
    private val cropInPlaceUC: CropInPlaceUC,
    private val cropOutsideUC: CropOutsideUC,
    private val nameUpdater: CropFileNameUpdater,
    private val toastRepo: ToastRepo,
    private val scheduler: Scheduler
) {
    private val canCropFileRepo = StateFlowRepo(false)
    private val nameSuggestionRepo = StateFlowRepo(NewNameSuggestion())
    private val dismissBus = PublishSubject.create<Boolean>()

    fun processIO(): ObservableTransformer<CropContract.Input, CropContract.Output> =
        scheduler.processIO(
            inputMapper = marInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<CropContract.Output> = {
        Observable.merge(
            listOf(
                dismissBus.map { CropContract.Output.Dismiss },
                canCropFileRepo.observe().map {
                    CropContract.Output.IsActionEnabled(it)
                },
                nameUpdater.observe().map {
                    val nameSuggestion =
                        it.value?.apply { canCropFileRepo.tryNext(true) } ?: NewNameSuggestion()
                    nameSuggestionRepo.tryNext(nameSuggestion)
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
                input.mapToUsecase<CropContract.Input.CheckCanCrop> { action ->
                    Completable.fromAction { nameSuggestionRepo.tryNext(action.nameSuggestion) }
                        .andThen(
                            canSaveAs.execute(
                                action.nameSuggestion.path,
                                canCropFileRepo
                            )
                        )
                },
                input.mapToUsecase<CropContract.Input.CropInPlace> {
                    cropInPlaceUC.execute(it.nameSuggestion).andThen { dismissBus.onNext(true) }
                },
                input.mapToUsecase<CropContract.Input.CropOutside> {
                    cropOutsideUC.execute(it.nameSuggestion)
                        .flatMapCompletable {
                            Completable.fromAction {
                                val toast =
                                    if (it.value == null) Stringer(R.string.toast_crop_failed) else {
                                        Stringer(R.string.toast_crop_success)
                                    }
                                toastRepo.onNext(toast)
                            }
                        }
                        .andThen { dismissBus.onNext(true) }

                }
            )
        )
    }
}
