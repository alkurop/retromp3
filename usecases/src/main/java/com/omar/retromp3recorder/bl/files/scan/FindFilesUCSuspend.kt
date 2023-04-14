package com.omar.retromp3recorder.bl.files.scan

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.utils.platform.DirPathProvider
import com.omar.retromp3recorder.utils.platform.FileEmptyChecker
import com.omar.retromp3recorder.utils.platform.FileLister
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class FindFilesUCSuspend @Inject constructor(
    private val fileEmptyChecker: FileEmptyChecker,
    private val dirPathProvider: DirPathProvider,
    private val fileLister: FileLister,
) {
    suspend fun execute(): List<ExistingFileWrapper> {
        return withContext(coroutineContext) {
            fileLister.listAudioFiles(dirPathProvider.fileDirs)
                .filter {
                    val isEmpty = fileEmptyChecker.isFileEmpty(it.path)
                    if (isEmpty) {
                        fileLister.deleteFile(it)
                        false
                    } else {
                        true
                    }
                }.sortedBy { it.createTimedStamp }
        }
    }
}
