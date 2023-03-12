package com.omar.retromp3recorder.utils.domain

import android.media.MediaMetadataRetriever
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.toFileWrapper
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileLister @Inject constructor() {
    val lister by lazy { MediaMetadataRetriever() }
    fun listFiles(
        dirPathList: List<String>,
        extensions: List<String>
    ): List<ExistingFileWrapper> {
        return dirPathList.map { listFiles(it, extensions) }.flatten()
    }

    fun discoverFile(path: String): ExistingFileWrapper {
        val file = File(path)
        return file.toFileWrapper().copy(length = discoverLength(path))
    }

    fun discoverLength(path: String): Long {
        return try {
            lister.setDataSource(path)
            lister.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
        } catch (e: Exception) {
            return 0
        }
    }

    private fun listFiles(dirPath: String, extensions: List<String>): List<ExistingFileWrapper> {
        val file = File(dirPath)
        return file.listFiles()?.filter { it.path.split(".").last() in extensions }?.map {
            val path = it.absolutePath
            it.toFileWrapper().copy(length = discoverLength(path))
        } ?: emptyList()
    }
}
