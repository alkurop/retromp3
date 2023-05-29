package com.omar.retromp3recorder.io.speech.downloader

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.omar.retromp3recorder.domain.RecognitionLanguage
import com.omar.retromp3recorder.io.downloader.FileDownloader
import com.omar.retromp3recorder.io.language.getFilename
import com.omar.retromp3recorder.io.language.getUrl
import com.omar.retromp3recorder.utils.domain.LoadingState
import com.omar.retromp3recorder.utils.platform.DirPathProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.util.concurrent.CountDownLatch


@HiltWorker
class LanguageDownloadWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val fileDownloadNotificationSender: LanguageDownloadNotificationSender,
    private val fileDownloader: FileDownloader,
    private val dirPathProvider: DirPathProvider,
    private val dispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val languageCode = inputData.getInt(LANGUAGE_CODE, NOT_FOUND)

        if (languageCode == NOT_FOUND) {
            return Result.failure()
        }

        val language = RecognitionLanguage.values()[languageCode]
        val url = language.getUrl()
        val fileName = language.getFilename()
        val path = dirPathProvider.provideModelDirPath()
        val destination = "$path/$fileName"

        val flow = fileDownloader.downloadLargeFile(url, destination)
        val latch = CountDownLatch(1)

        lateinit var result: Result
        coroutineScope {
            flow.flowOn(dispatcher).collect { next ->
                when (next) {
                    is LoadingState.Failed -> {
                        fileDownloadNotificationSender.sendNotification(
                            LanguageDownloadStatus.FinishedWithError(language, next.cause)
                        )
                        runCatching { File(destination).delete() }
                        latch.countDown()
                        result = Result.failure(workDataOf(FAILURE_CAUSE to next.cause.toString()))
                    }
                    is LoadingState.Loading -> {
                        fileDownloadNotificationSender.sendNotification(
                            LanguageDownloadStatus.LoadingProgress(language, next.progress)
                        )
                        setProgress(workDataOf(PROGRESS to next.progress))
                    }
                    is LoadingState.Success -> {
                        latch.countDown()
                        result = Result.success(workDataOf(LANGUAGE_CODE to languageCode))
                    }
                }
            }
        }
        latch.await()
        return result
    }

    companion object {
        const val WORKER_NAME = "LanguageDownloadWorker"
        const val LANGUAGE_CODE = "LANGUAGE_CODE"
        const val FAILURE_CAUSE = "FAILURE_CAUSE"
        const val PROGRESS = "PROGRESS"
        const val NOT_FOUND = -1
    }
}


