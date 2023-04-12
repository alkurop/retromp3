package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.audio.actions.PlayerObserveSettingsUC
import com.omar.retromp3recorder.bl.files.NewCurrentFileUpdater
import com.omar.retromp3recorder.bl.files.TakeLastFileDirScanUC
import com.omar.retromp3recorder.bl.settings.FeatureMapLoadUC
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUC
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartupUC @Inject constructor(
    private val takeLastFileWithScanDirScanUC: TakeLastFileDirScanUC,
    private val newCurrentFileUpdater: NewCurrentFileUpdater,
    private val loadRecorderSettingsUC: LoadRecorderSettingsUC,
    private val playerObserveSettingsUC: PlayerObserveSettingsUC,
    private val featureMapLoadUC: FeatureMapLoadUC,
    private val jobWrapper: ScopeJobWrapper
) {
    fun execute() {
        jobWrapper.launch { takeLastFileWithScanDirScanUC.execute() }
        jobWrapper.launch { newCurrentFileUpdater.execute() }
        jobWrapper.launch { loadRecorderSettingsUC.execute() }
        jobWrapper.launch { featureMapLoadUC.execute() }
        jobWrapper.launch { playerObserveSettingsUC.execute() }
    }
}
