package com.omar.retromp3recorder.io.speech.downloader

import androidx.work.*
import com.omar.retromp3recorder.domain.RecognitionLanguage
import javax.inject.Inject

class DownloadLanguageHook @Inject constructor(
    private val workManager: WorkManager

) {
    fun execute(recognitionLanguage: RecognitionLanguage) {
        val uniqueWorkName = recognitionLanguage.uniqueWorkName()

        val work = OneTimeWorkRequestBuilder<LanguageDownloadWorker>()
            .setInputData(workDataOf(LanguageDownloadWorker.LANGUAGE_CODE to recognitionLanguage.ordinal))
            .addTag(LanguageDownloadWorker.WORKER_NAME)
            .addTag(uniqueWorkName)
            .build()

        workManager
            .enqueueUniqueWork(uniqueWorkName, ExistingWorkPolicy.KEEP, work)
    }
}

fun RecognitionLanguage.uniqueWorkName() = LanguageDownloadWorker.LANGUAGE_CODE + this.ordinal
