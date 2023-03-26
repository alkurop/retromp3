package com.omar.retromp3recorder.utils.platform

import java.io.File
import javax.inject.Inject

class FileEmptyChecker @Inject constructor(
    private val fileLister: FileLister
)  {
    fun isFileEmpty(filePath: String): Boolean {
        val file = File(filePath)
        val audioDurationForExistingFile =
            fileLister.discoverLength(filePath)
        return (file.exists()
                && file.length() > 0).not()
                || audioDurationForExistingFile < 10L
    }
}
