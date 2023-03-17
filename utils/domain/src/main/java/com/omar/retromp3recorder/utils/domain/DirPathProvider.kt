package com.omar.retromp3recorder.utils.domain

import android.content.Context
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class DirPathProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun providerDirPath(): String {
        return fileDirs.first { File(it).exists() }
    }

    val fileDirs: List<String>
        get() = listOf(
            context.filesDir.toString(),
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/RetroMp3Recorder",
            context.externalCacheDir.toString(),
            context.cacheDir.toString()
        )
}
