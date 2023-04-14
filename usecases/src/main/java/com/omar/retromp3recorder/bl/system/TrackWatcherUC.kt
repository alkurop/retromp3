package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.audio.actions.PlayerObserveSettingsUC
import com.omar.retromp3recorder.bl.files.NewCurrentFileUpdater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackWatcherUC @Inject constructor(
    private val newCurrentFileUpdater: NewCurrentFileUpdater,
    private val playerObserveSettingsUC: PlayerObserveSettingsUC,
) {
    fun execute(scope: CoroutineScope) {
        scope.launch { newCurrentFileUpdater.execute() }
        scope.launch { playerObserveSettingsUC.execute() }
    }
}
