package com.omar.retromp3recorder.app.ui.menu.popups.crop.logic

import com.omar.retromp3recorder.bl.actions.CropUC
import com.omar.retromp3recorder.dto.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.local.MenuPopupBus
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CropOutsideUC @Inject constructor(
    private val menuPopupBus: MenuPopupBus,
    private val cropUC: CropUC,
) {
    fun execute(nameSuggestion: NewNameSuggestion): Completable = Completable.fromAction {
        menuPopupBus.onNext(Optional.empty())
    }
}