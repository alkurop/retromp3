package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.files.TakeLastFileDirScanUC
import com.omar.retromp3recorder.bl.settings.FeatureMapLoadUC
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUC
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StartupUC @Inject constructor(
    private val takeLastFileWithScanDirScanUC: TakeLastFileDirScanUC,
    private val loadRecorderSettingsUC: LoadRecorderSettingsUC,
    private val featureMapLoadUC: FeatureMapLoadUC,
    private val jobWrapper: ScopeJobWrapper
) {
    suspend fun execute() {
        withContext(jobWrapper.coroutineContext) {
            takeLastFileWithScanDirScanUC.execute()
            loadRecorderSettingsUC.execute()
            featureMapLoadUC.execute()
        }
    }
}
