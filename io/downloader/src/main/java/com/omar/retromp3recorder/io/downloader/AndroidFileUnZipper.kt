package com.omar.retromp3recorder.io.downloader

import com.omar.retromp3recorder.utils.domain.LoadingState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipFile
import javax.inject.Inject

internal class AndroidFileUnZipper @Inject constructor() : FileUnZipper {
    override suspend fun unzipFlow(
        filePath: String,
        destination: String
    ): Flow<LoadingState<File>> {
        return flow {
            emit(LoadingState.Loading(0))
            runCatching {
                val destinationDir = File(destination)
                if (destinationDir.exists().not()) {
                    destinationDir.mkdir()
                }
                val input = ZipFile(filePath)
                val entries = input.entries()

                val total = input.size()
                var count = 0

                fun percent(count: Int, total: Int): Int = (count * 100 / total)

                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    if (entry.isDirectory) {
                        File(destination, entry.name)
                    } else {
                        BufferedOutputStream(FileOutputStream(File(destination, entry.name)))
                            .use { output ->
                                input.getInputStream(entry).use { data ->
                                    output.write(data.readBytes())
                                }
                            }
                        count++
                        emit(LoadingState.Loading(percent(count, total)))
                    }
                }
                emit(LoadingState.Success(destinationDir))
            }.exceptionOrNull()?.let { emit(LoadingState.Failed(it)) }
        }
    }
}
