package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.files.NewCurrentFileUpdaterSuspend
import com.omar.retromp3recorder.bl.files.TakeLastFileDirScanUCSuspend
import com.omar.retromp3recorder.bl.settings.FeatureMapLoadUCSuspend
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUCSuspend
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartupUCSuspend @Inject constructor(
    private val takeLastFileWithScanDirScanUC: TakeLastFileDirScanUCSuspend,
    private val newCurrentFileUpdaterSuspend: NewCurrentFileUpdaterSuspend,
    private val loadRecorderSettingsUCSuspend: LoadRecorderSettingsUCSuspend,
    private val featureMapLoadUCSuspend: FeatureMapLoadUCSuspend,
    private val jobWrapper: ScopeJobWrapper
) {
    fun execute() {
        jobWrapper.launch {
            takeLastFileWithScanDirScanUC.execute()
        }
        jobWrapper.launch {
            newCurrentFileUpdaterSuspend.execute()
        }
        jobWrapper.launch { loadRecorderSettingsUCSuspend.execute() }
        jobWrapper.launch { featureMapLoadUCSuspend.execute() }
    }
}
