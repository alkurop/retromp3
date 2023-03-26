package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.bl.files.scan.ScanDirFilesPartialUCSuspend
import com.omar.retromp3recorder.utils.platform.ScopeJobWrapper
import kotlinx.coroutines.launch
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
class TakeLastFileDirScanUCSuspend @Inject constructor(
    private val fileRepoUpdaterUC: FileRepoUpdaterUCSuspend,
    private val scanDirFilesPartialUC: ScanDirFilesPartialUCSuspend,
    private val takeLastFileFastUC: TakeLastFileDbItemUCSuspend,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute() {
        scopeJobWrapper.launch {
            val databaseFile = takeLastFileFastUC.execute()
            if (databaseFile == null) {
                scanDirFilesPartialUC.execute()
            } else {
                fileRepoUpdaterUC.execute(listOf(databaseFile))
                scanDirFilesPartialUC.execute()
            }
        }
    }
}

