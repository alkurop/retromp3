package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.dto.AudioEnabler
import com.omar.retromp3recorder.dto.MenuAction
import com.omar.retromp3recorder.dto.VisibilityEnabler
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class EnablersSwitcher @Inject constructor(
    private val audioEnablerMapper: AudioEnablerMapper,
    private val visibilityEnablerMapper: VisibilityEnablerMapper
) {
    fun execute(enable: MenuAction.Enable): Completable =
        when (val enabler = enable.enabler) {
            is AudioEnabler -> {
                val (_, shouldEnable) = enable
                audioEnablerMapper.execute(shouldEnable, enabler)
            }
            is VisibilityEnabler -> {
                val (_, shouldEnable) = enable
                visibilityEnablerMapper.execute(shouldEnable, enabler)
            }
        }
}
