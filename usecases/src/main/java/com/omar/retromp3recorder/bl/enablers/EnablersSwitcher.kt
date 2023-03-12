package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.domain.AudioEnabler
import com.omar.retromp3recorder.domain.MenuEnabler
import com.omar.retromp3recorder.domain.VisibilityEnabler
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class EnablersSwitcher @Inject constructor(
    private val audioEnablerMapper: AudioEnablerMapper,
    private val visibilityEnablerMapper: VisibilityEnablerMapper
) {
    fun execute(enabler: MenuEnabler, isEnabled: Boolean): Completable =
        when (enabler) {
            is AudioEnabler -> {
                audioEnablerMapper.execute(enabler, isEnabled)
            }
            is VisibilityEnabler -> {
                visibilityEnablerMapper.execute(enabler, isEnabled)
            }
        }
}
