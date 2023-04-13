package com.omar.retromp3recorder.utils.platform

import android.content.Context
import android.os.Environment
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class DirPathProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val jobWrapper: ScopeJobWrapper,
) {
    fun providerDirPath(): String {
        return fileDirs.first { File(it).exists() }
    }

    suspend fun provideModelDirPath(): String {
        val root = fileDirs.first { File(it).exists() }
        val folder = "models"
        val folderPath = "$root/$folder"
        withContext(jobWrapper.coroutineContext) {
            val file = File(folderPath)
            if (file.exists().not()) {
                val result = file.mkdir()
                if (!result) throw Error("Failed to create morel dir")
            }
        }
        return folderPath
    }

    val fileDirs: List<String>
        get() = listOf(
            context.filesDir.toString(),
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/RetroMp3Recorder",
            context.externalCacheDir.toString(),
            context.cacheDir.toString()
        )
}
