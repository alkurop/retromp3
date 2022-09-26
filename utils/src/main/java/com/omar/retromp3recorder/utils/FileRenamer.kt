package com.omar.retromp3recorder.utils

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import java.io.File
import javax.inject.Inject


class FileRenamer @Inject constructor() {
    fun renameFile(fileWrapper: ExistingFileWrapper, newName: String): String {
        val newFile = generateNewFile(fileWrapper, newName)
        val isRenamed = File(fileWrapper.path).renameTo(newFile)

        return if (isRenamed) newFile.path else fileWrapper.path
    }

    fun canRename(fileWrapper: ExistingFileWrapper, newName: String): Boolean {
        val generateNewFile = generateNewFile(fileWrapper, newName)
        return generateNewFile.exists().not()
    }

    private fun generateNewFile(fileWrapper: ExistingFileWrapper, newName: String): File {
        val split = fileWrapper.path.split("/")
        val pathWithoutName = split.dropLast(1).joinToString(separator = "/")
        val oldFileName = split.last()
        val ext = oldFileName.split(".").last()
        val newFileName = "$newName.$ext"
        return File("$pathWithoutName/$newFileName")
    }
}