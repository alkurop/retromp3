package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.bl.actions.ActionsMapper
import com.omar.retromp3recorder.bl.enablers.EnablersReverseMapper
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class MenuInteractor @Inject constructor(
    private val actionsMapper: ActionsMapper,
    private val reverseMapper: EnablersReverseMapper,
    private val workScheduler: Scheduler,

    ) {
//    fun processIO():Observable<>
}
