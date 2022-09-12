package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.bl.system.WaveformScanUpdaterUC
import com.omar.retromp3recorder.dto.isEmpty
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Runs the FindFilesUC
 *
 * Tasks the last file from file list repo
 * (we expect it to be the last recording)
 *
 * and puts it into the CurrentFileRepo
 * use TakeLastFileNoScanUC. this version is very slow when user has a lot of files
 */
class TakeLastFileDirScanUC @Inject constructor(
    private val fileRepoUpdaterUC: FileRepoUpdaterUC,
    private val findFilesUC: ScanDirFilesUC,
    private val scheduler: Scheduler,
    private val waveformScan: WaveformScanUpdaterUC
) {
    fun execute(): Completable {
        return findFilesUC.execute()
            .flatMapCompletable { updatedList ->
                Completable.concat(
                    listOf(
                        fileRepoUpdaterUC.execute(updatedList),
                        Single.fromCallable { updatedList.filter { it.wavetable.isEmpty() } }
                            .flatMapCompletable { listForWaveform ->
                                waveformScan.execute(listForWaveform)
                            }
                    ))
            }
            .subscribeOn(scheduler)
    }
}

