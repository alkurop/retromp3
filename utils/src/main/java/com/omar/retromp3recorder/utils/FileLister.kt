package com.omar.retromp3recorder.utils

import android.media.MediaMetadataRetriever
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.toFileWrapper
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface FileLister {
    fun listFiles(dirPathList: List<String>): List<ExistingFileWrapper>
    fun discoverFile(path: String): ExistingFileWrapper
    fun discoverLength(path: String): Long
}

@Singleton
class FileListerImpl @Inject constructor() : FileLister {
    val lister by lazy { MediaMetadataRetriever() }
    override fun listFiles(dirPathList: List<String>): List<ExistingFileWrapper> {
        return dirPathList.map { listFiles(it) }.flatten()
    }

    override fun discoverFile(path: String): ExistingFileWrapper {
        val file = File(path)
        return file.toFileWrapper()
    }

    override fun discoverLength(path: String): Long {
        lister.setDataSource(path)
        return lister.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
    }

    private fun listFiles(dirPath: String): List<ExistingFileWrapper> {
        val file = File(dirPath)
        return file.listFiles()?.map { it.toFileWrapper() }
            ?: emptyList()
    }
}
