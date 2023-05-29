package com.omar.retromp3recorder.io.speech.downloader

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.omar.retromp3recorder.domain.RecognitionLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DownloadLanguageHook @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun execute(recognitionLanguage: RecognitionLanguage) {
        val uniqueWorkName = recognitionLanguage.uniqueWorkName()

        val downloadWork = OneTimeWorkRequest.Builder(LanguageDownloadWorker::class.java)
            .setInputData(workDataOf(LanguageDownloadWorker.LANGUAGE_CODE to recognitionLanguage.ordinal))
            .addTag(LanguageDownloadWorker.WORKER_NAME)
            .addTag(uniqueWorkName)
            .build()

        val unzipWork = OneTimeWorkRequest.Builder(LanguageUnzipWorker::class.java)
            .setInputData(workDataOf(LanguageUnzipWorker.LANGUAGE_CODE to recognitionLanguage.ordinal))
            .addTag(LanguageUnzipWorker.WORKER_NAME)
            .addTag(uniqueWorkName)
            .build()

        WorkManager.getInstance(context)
            .beginUniqueWork(uniqueWorkName, ExistingWorkPolicy.KEEP, downloadWork)
            .then(unzipWork)
            .enqueue()

    }
}

fun RecognitionLanguage.uniqueWorkName() = LanguageDownloadWorker.LANGUAGE_CODE + this.ordinal
