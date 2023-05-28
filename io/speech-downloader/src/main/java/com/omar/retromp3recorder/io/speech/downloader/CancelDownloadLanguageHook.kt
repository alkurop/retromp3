package com.omar.retromp3recorder.io.speech.downloader

import androidx.work.WorkManager
import com.omar.retromp3recorder.domain.RecognitionLanguage
import javax.inject.Inject

class CancelDownloadLanguageHook @Inject constructor(
    private val workManager: WorkManager

) {
    fun execute(recognitionLanguage: RecognitionLanguage) {
        val uniqueWorkName = recognitionLanguage.uniqueWorkName()
        workManager
            .cancelAllWorkByTag(uniqueWorkName)
    }
}

