package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.utils.platform.FileRenamer
import javax.inject.Inject

class CanSaveAsNameUC @Inject constructor(
    private val fileRenamer: FileRenamer
) {
    fun execute(path: String): Boolean {
        return fileRenamer.exists(path).not()
    }
}
