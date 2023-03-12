package com.omar.retromp3recorder.app.ui.menu.popups.crop.logic

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.bl.actions.CropUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import com.omar.retromp3recorder.storage.repo.local.MenuPopupBus
import com.omar.retromp3recorder.domain.platform.Optional
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CropOutsideUC @Inject constructor(
    private val menuPopupBus: MenuPopupBus,
    private val toastRepo: ToastRepo,
    private val cropUC: CropUC,
) {
    fun execute(nameSuggestion: NewNameSuggestion): Single<Optional<ExistingFileWrapper>> =
        cropUC
            .execute(nameSuggestion)
            .flatMap {
                Completable
                    .fromAction {
                        menuPopupBus.onNext(Optional.empty())
                        val toast =
                            if (it.value == null) Stringer(R.string.toast_crop_failed) else {
                                Stringer(R.string.toast_crop_success)
                            }
                        toastRepo.onNext(toast)
                    }
                    .andThen(Single.just(it))
            }
}
