package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.audio.speech.AvailableLanguageListUC
import com.omar.retromp3recorder.bl.files.TakeLastFileDirScanUC
import com.omar.retromp3recorder.bl.settings.FeatureMapLoadUC
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUC
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import javax.inject.Inject

class StartupUC @Inject constructor(
    private val takeLastFileWithScanDirScanUC: TakeLastFileDirScanUC,
    private val loadRecorderSettingsUC: LoadRecorderSettingsUC,
    private val availableLanguageListUC: AvailableLanguageListUC,
    private val featureMapLoadUC: FeatureMapLoadUC,
    private val currentFileRepo: CurrentFileRepo,
) {
    suspend fun execute() {
        val value = currentFileRepo.first().value
        if (value == null) {
            takeLastFileWithScanDirScanUC.execute()
            loadRecorderSettingsUC.execute()
            featureMapLoadUC.execute()
            availableLanguageListUC.execute()
        }
    }
}
