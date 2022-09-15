package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.utils.FileEmptyChecker
import com.omar.retromp3recorder.utils.FileLister
import com.omar.retromp3recorder.utils.FilePathGenerator
import io.reactivex.rxjava3.core.Single
import java.io.File
import javax.inject.Inject

class FindFilesUC @Inject constructor(
    private val fileEmptyChecker: FileEmptyChecker,
    private val filePathGenerator: FilePathGenerator,
    private val fileLister: FileLister,
) {
    fun get(
        extensions: List<String>,
        shouldCheckEmptyFiles: Boolean = true
    ): Single<List<ExistingFileWrapper>> = Single.fromCallable {
        val foundFiles = fileLister.listFiles(filePathGenerator.fileDirs, extensions)

        val nonEmptyFiles =
            if (shouldCheckEmptyFiles) {
                foundFiles.filter { fileEmptyChecker.isFileEmpty(it.path).not() }
                    .also { nonEmptyFiles ->
                        foundFiles.filter { it !in nonEmptyFiles }
                            .forEach { File(it.path).delete() }
                    }
            } else {
                foundFiles
            }
        nonEmptyFiles.sortedBy { it.createTimedStamp }
    }

}
