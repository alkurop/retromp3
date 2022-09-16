package com.omar.retromp3recorder.bl.files

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
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
    private val scanDirFilesPartialUC: ScanDirFilesPartialUC,
    private val takeLastFileFastUC: TakeLastFileDbItemUC,
    private val scheduler: Scheduler,
) {
    fun execute(): Completable {
        return takeLastFileFastUC.get()
            .flatMapCompletable { item ->
                val value = item.value
                if (value == null) {
                    scanDirFilesPartialUC.execute()
                } else fileRepoUpdaterUC
                    .execute(listOf(value))
                    .andThen(scanDirFilesPartialUC.execute())
            }.subscribeOn(scheduler)
    }
}

