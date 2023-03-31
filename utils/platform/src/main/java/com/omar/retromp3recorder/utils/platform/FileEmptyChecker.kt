package com.omar.retromp3recorder.utils.platform

import java.io.File
import javax.inject.Inject

class FileEmptyChecker @Inject constructor(
    private val fileLister: FileLister
) {
    fun isFileEmpty(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists().not() || fileLister.discoverLength(filePath) == 0L
    }
}
