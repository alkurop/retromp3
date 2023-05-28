package com.omar.retromp3recorder.io.speech.downloader

import android.content.Context
import androidx.work.WorkManager
import com.omar.retromp3recorder.domain.RecognitionLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CancelDownloadLanguageHook @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun execute(recognitionLanguage: RecognitionLanguage) {
        val uniqueWorkName = recognitionLanguage.uniqueWorkName()
        WorkManager.getInstance(context)
            .cancelAllWorkByTag(uniqueWorkName)
    }
}

