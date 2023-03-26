package com.omar.retromp3recorder.bl.files.scan

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.utils.platform.FileLister
import com.omar.retromp3recorder.utils.platform.DirPathProvider
import com.omar.retromp3recorder.utils.platform.FileEmptyChecker
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class FindFilesUCSuspend @Inject constructor(
    private val fileEmptyChecker: FileEmptyChecker,
    private val dirPathProvider: DirPathProvider,
    private val fileLister: FileLister,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(
        extensions: List<String>,
        shouldCheckEmptyFiles: Boolean = true
    ): List<ExistingFileWrapper> {
        scopeJobWrapper.cancelAndJoin()
        val foundFiles = withContext(scopeJobWrapper.coroutineContext) {
            fileLister.listFiles(dirPathProvider.fileDirs, extensions)
        }

        val nonEmptyFiles = if (shouldCheckEmptyFiles) {
                foundFiles.filter { fileEmptyChecker.isFileEmpty(it.path).not() }
                    .also { nonEmptyFiles ->
                        foundFiles.filter { it !in nonEmptyFiles }
                            .forEach { File(it.path).delete() }
                    }
            } else {
                foundFiles
            }
        return nonEmptyFiles.sortedBy { it.createTimedStamp }
    }
}
