package com.omar.retromp3recorder.io.speech.downloader

import android.content.Context
import androidx.work.WorkManager
import com.omar.retromp3recorder.domain.LanguageState
import com.omar.retromp3recorder.domain.RecognitionLanguage
import com.omar.retromp3recorder.io.speech.downloader.LanguageDownloadWorker.Companion.PROGRESS
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CheckLanguagesInDownload @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun execute(): List<Pair<RecognitionLanguage, LanguageState.DownLoading>> {
        return coroutineScope {
            val currentJobs = WorkManager.getInstance(context)
                .getWorkInfosByTag(LanguageDownloadWorker.WORKER_NAME)
                .runCatching {
                    get()
                }.getOrDefault(emptyList())
                .filter { it.state.isFinished.not() }


            RecognitionLanguage.values()
                .mapNotNull { language ->
                    (currentJobs.firstOrNull { it.tags.contains(language.uniqueWorkName()) }?.progress
                        ?.getInt(PROGRESS, 0)?.let { language to LanguageState.DownLoading(it) })
                }
        }
    }
}

