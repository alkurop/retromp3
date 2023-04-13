package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.audio.actions.PlayerObserveSettingsUC
import com.omar.retromp3recorder.bl.files.NewCurrentFileUpdater
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackWatcherUC @Inject constructor(
    private val newCurrentFileUpdater: NewCurrentFileUpdater,
    private val playerObserveSettingsUC: PlayerObserveSettingsUC,
    private val jobWrapper: ScopeJobWrapper
) {
    fun execute() {
        jobWrapper.launch { newCurrentFileUpdater.execute() }
        jobWrapper.launch { playerObserveSettingsUC.execute() }
    }
}
