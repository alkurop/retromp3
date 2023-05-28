package com.omar.retromp3recorder.io.downloader

import com.omar.retromp3recorder.utils.domain.LoadingState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FileDownloader {
  suspend  fun downloadLargeFile(sourcePath: String, destinationPath: String): Flow<LoadingState<File>>
}
