package com.omar.retromp3recorder.io.downloader

import com.omar.retromp3recorder.utils.domain.LoadingState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FileUnZipper {

    suspend fun unzipFlow(filePath: String, destination: String): Flow<LoadingState<File>>
}
