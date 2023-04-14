package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.bl.files.scan.ScanDirFilesPartialUCSuspend
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.toOptional
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
    private val currentFileRepo: CurrentFileRepo,
    private val scanDirFilesPartialUC: ScanDirFilesPartialUCSuspend,
    private val takeLastFileFastUC: TakeLastFileDbItemUC,
) {
    suspend fun execute() {
        val databaseFile = takeLastFileFastUC.execute()
        if (databaseFile != null) {
            currentFileRepo.emit(databaseFile.toOptional())
        }
        scanDirFilesPartialUC.execute()
    }
}

