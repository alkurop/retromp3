package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MenusExcavator @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    fun observable(): Observable<Menu>
}
