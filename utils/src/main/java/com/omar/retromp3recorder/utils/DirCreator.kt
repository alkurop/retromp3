package com.omar.retromp3recorder.utils

import android.os.Build
import timber.log.Timber
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject


class DirCreator @Inject constructor() {
    fun createDirIfNotExists(filepath: String) {
        val f = File(filepath)
        if (f.exists().not()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Files.createDirectory(Paths.get(f.absolutePath))
                } else {
                    f.mkdir()
                    f.mkdirs()
                }
            } catch (
                e: Throwable
            ) {
                Timber.e(e)
            }
        }
        val exists = f.exists()
        Timber.d("File with path $filepath exists = $exists")
    }
}