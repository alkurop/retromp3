package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.dto.MenuAction
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class EnablersReverseMapper @Inject constructor(
    private val enablersMapper: EnablersMapper
) {
    fun execute(enable: MenuAction.Enable): Completable =
        enablersMapper.execute(enable.copy(isEnabled = !enable.isEnabled))
}
