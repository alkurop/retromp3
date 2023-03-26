package com.omar.retromp3recorder.utils.platform

import java.io.File
import javax.inject.Inject


class FileDeleter @Inject constructor() {
    fun deleteFile(filePath: String) {
        val file = File(filePath)
        if (file.exists())
            file.delete()
    }
}
