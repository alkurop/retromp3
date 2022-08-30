package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.bl.audio.PlayerProgressMapper
import com.omar.retromp3recorder.bl.audio.RecordWavetableUC
import com.omar.retromp3recorder.bl.audio.StopRecordAfterTooLongUC
import com.omar.retromp3recorder.bl.files.NewFileUpdater
import com.omar.retromp3recorder.bl.files.ScanDirFilesUC
import com.omar.retromp3recorder.bl.files.TakeLastFileDirScanUC
import com.omar.retromp3recorder.bl.settings.FeatureMapLoadUC
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUC
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class StartupUC @Inject constructor(
    private val cleanUpSeekRepoUsecase: NewFileUpdater,
    private val featureMapLoadUC: FeatureMapLoadUC,
    private val joinPlayerProgressMapper: JoinedProgressMapper,
    private val loadRecorderSettingsUC: LoadRecorderSettingsUC,
    private val playerProgressMapper: PlayerProgressMapper,
    private val scanDirFilesUC: ScanDirFilesUC,
    private val stopRecordAfterTooLongUC: StopRecordAfterTooLongUC,
    private val takeLastFileWithScanDirScanUC: TakeLastFileDirScanUC,
    private val wakelockUsecase: WakeLockUsecase,
    private val wavetableUC: RecordWavetableUC
) {
    fun execute(): Completable = Completable.merge(
        listOf(
            cleanUpSeekRepoUsecase.execute(),
            featureMapLoadUC.execute(),
            joinPlayerProgressMapper.observe(),
            loadRecorderSettingsUC.execute(),
            playerProgressMapper.execute(),
            scanDirFilesUC.execute(),
            stopRecordAfterTooLongUC.execute(),
            takeLastFileWithScanDirScanUC.execute(),
            wakelockUsecase.execute(),
            wavetableUC.execute(),
        )
    )
}
