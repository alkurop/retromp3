package com.omar.retromp3recorder.utils.platform

import android.media.MediaMetadataRetriever
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.toFileWrapper
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileLister @Inject constructor() {
    private val lister by lazy { MediaMetadataRetriever() }

    fun listAudioFiles(
        dirPathList: List<String>
    ): List<ExistingFileWrapper> {
        return dirPathList.map { listFiles(it, listOf("mp3")) }.flatten()
    }

    fun discoverFile(path: String): ExistingFileWrapper {
        val file = File(path)
        val length = discoverLength(path)
        return file.toFileWrapper().copy(length = length)
    }

    fun discoverLength(path: String): Long {
        return try {
            lister.setDataSource(path)
            val extract = lister.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            extract?.toLong() ?: 0
        } catch (e: Exception) {
            Timber.e(e)
            0
        }
    }

    fun deleteFile(file: ExistingFileWrapper) {
        File(file.path).delete()
    }

    private fun listFiles(dirPath: String, extensions: List<String>): List<ExistingFileWrapper> {
        val file = File(dirPath)
        return file.listFiles()?.filter { it.path.split(".").last() in extensions }?.map {
            val path = it.absolutePath
            it.toFileWrapper().copy(length = discoverLength(path))
        } ?: emptyList()
    }
}
