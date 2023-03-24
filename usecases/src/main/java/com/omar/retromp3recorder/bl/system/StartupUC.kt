package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.audio.progress.PlayerProgressMapper
import com.omar.retromp3recorder.bl.files.NewCurrentFileUpdater
import com.omar.retromp3recorder.bl.files.TakeLastFileDirScanUC
import com.omar.retromp3recorder.bl.settings.FeatureMapLoadUC
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUC
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class StartupUC @Inject constructor(
    private val onNewFileUC: NewCurrentFileUpdater,
    private val featureMapLoadUC: FeatureMapLoadUC,
    private val loadRecorderSettingsUC: LoadRecorderSettingsUC,
    private val playerProgressMapper: PlayerProgressMapper,
    private val takeLastFileWithScanDirScanUC: TakeLastFileDirScanUC,
) {
    fun execute(): Completable = Completable.merge(
        listOf(
            onNewFileUC.execute(),
            featureMapLoadUC.execute(),
            loadRecorderSettingsUC.execute(),
            playerProgressMapper.execute(),
            takeLastFileWithScanDirScanUC.execute(),
        )
    )
}
