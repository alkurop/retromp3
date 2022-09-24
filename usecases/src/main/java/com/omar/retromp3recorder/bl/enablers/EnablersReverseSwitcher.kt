package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.dto.MenuAction
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class EnablersReverseSwitcher @Inject constructor(
    private val enablersSwitcher: EnablersSwitcher
) {
    fun execute(enable: MenuAction.Enable): Completable =
        enablersSwitcher.execute(enable.copy(isEnabled = !enable.isEnabled))
}
