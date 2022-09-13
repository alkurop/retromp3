package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.bl.system.WaveformScanUpdaterUC
import com.omar.retromp3recorder.dto.isEmpty
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntity
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.utils.Optional
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
    private val waveformScan: WaveformScanUpdaterUC,
    private val appDatabase: AppDatabase,
) {
    fun execute(): Completable {
        return Single.fromCallable { Optional(appDatabase.fileEntityDao().takeLast()) }
            .flatMapCompletable { item ->
                val value = item.value
                val findFiles = findFilesUC.execute()
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
                if (value == null) {
                    findFiles
                } else fileRepoUpdaterUC.execute(listOf(value.toFileWrapper())).andThen(findFiles)
            }.subscribeOn(scheduler)
    }
}

