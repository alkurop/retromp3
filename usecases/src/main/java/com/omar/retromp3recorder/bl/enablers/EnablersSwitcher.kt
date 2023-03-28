package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.domain.AudioEnabler
import com.omar.retromp3recorder.domain.MenuEnabler
import com.omar.retromp3recorder.domain.VisibilityEnabler
import javax.inject.Inject

class EnablersSwitcher @Inject constructor(
    private val audioEnablerMapper: AudioEnablerMapper,
    private val visibilityEnablerMapper: VisibilityEnablerMapper
) {
    suspend fun execute(enabler: MenuEnabler, isEnabled: Boolean) {
        when (enabler) {
            is AudioEnabler -> {
                audioEnablerMapper.execute(enabler, isEnabled)
            }
            is VisibilityEnabler -> {
                visibilityEnablerMapper.execute(enabler, isEnabled)
            }
        }
    }
}
