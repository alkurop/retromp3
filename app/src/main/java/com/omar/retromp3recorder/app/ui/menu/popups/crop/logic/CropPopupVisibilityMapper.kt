package com.omar.retromp3recorder.app.ui.menu.popups.crop.logic

import com.omar.retromp3recorder.dto.MenuExecutable
import com.omar.retromp3recorder.storage.repo.local.MenuPopupBus
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CropPopupVisibilityMapper @Inject constructor(
    private val popupBus: MenuPopupBus
) {
    fun observe(): Observable<Boolean> = popupBus.observe().map {
        it.value == MenuExecutable.Crop
    }
}