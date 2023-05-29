package com.omar.retromp3recorder.io.downloader

import com.omar.retromp3recorder.utils.domain.LoadingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipFile
import javax.inject.Inject

internal class AndroidFileUnZipper @Inject constructor() : FileUnZipper {
    override suspend fun unzipFlow(
        filePath: String,
        destination: String
    ): Flow<LoadingState<File>> {
        return flow<LoadingState<File>> {
            emit(LoadingState.Loading())
            runCatching {
                lateinit var fileName: String
                ZipFile(filePath).use { zip ->
                    fileName = zip.name
                    val sequence = zip.entries().asSequence()
                    sequence.forEach { entry ->
                        zip.getInputStream(entry).use { input ->
                            val entryPath = "$destination/${entry.name}"

                            if (!entry.isDirectory) {
                                // if the entry is a file, extracts it
                                extractFile(input, entryPath)
                            } else {
                                // if the entry is a directory, make the directory
                                val dir = File(entryPath)
                                dir.mkdir()
                            }
                        }
                    }
                }
                emit(LoadingState.Success(File(destination, fileName)))
            }.exceptionOrNull()?.let { emit(LoadingState.Failed(it)) }
        }.distinctUntilChanged()
    }
}

private fun extractFile(inputStream: InputStream, destFilePath: String) {
    val bos = BufferedOutputStream(FileOutputStream(destFilePath))
    val bytesIn = ByteArray(BUFFER_SIZE)
    var read: Int
    while (inputStream.read(bytesIn).also { read = it } != -1) {
        bos.write(bytesIn, 0, read)
    }
    bos.close()
}

private const val BUFFER_SIZE: Int = 4096
