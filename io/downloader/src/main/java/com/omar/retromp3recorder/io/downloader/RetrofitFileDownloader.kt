package com.omar.retromp3recorder.io.downloader

import com.omar.retromp3recorder.utils.domain.LoadingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

internal class RetrofitFileDownloader @Inject constructor(
    private val fileApi: FileApi,
) : FileDownloader {
    override fun downloadLargeFile(
        sourcePath: String,
        destinationPath: String
    ): Flow<LoadingState<File>> {
        return fileApi.getFile(sourcePath)
            .saveFileFlow(destinationPath)
    }

    @Suppress("UNUSED") // provided for example
    private fun ResponseBody.saveFile(destination: String) {
        val destinationFile = File(destination)
        byteStream().use { inputStream ->
            destinationFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    private fun ResponseBody.saveFileFlow(destination: String): Flow<LoadingState<File>> {
        return flow {
            emit(LoadingState.Loading(0))
            val destinationFile = File(destination)
            try {
                byteStream().use { inputStream ->
                    destinationFile.outputStream().use { outputStream ->
                        val totalBytes = contentLength()
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progressBytes = 0L
                        var bytes = inputStream.read(buffer)
                        while (bytes >= 0) {
                            outputStream.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = inputStream.read(buffer)
                            emit(LoadingState.Loading(((progressBytes * 100) / totalBytes).toInt()))
                        }
                    }
                }
                emit(LoadingState.Success(destinationFile))
            } catch (e: Exception) {
                emit(LoadingState.Failed(e))
            }
        }
    }
}
