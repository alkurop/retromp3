package com.omar.retromp3recorder.io.speech.downloader

import androidx.work.WorkManager
import com.omar.retromp3recorder.domain.LanguageState
import com.omar.retromp3recorder.domain.RecognitionLanguage
import com.omar.retromp3recorder.io.speech.downloader.LanguageDownloadWorker.Companion.PROGRESS
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CheckLanguagesInDownload @Inject constructor(
    private val workManager: WorkManager
) {
    suspend fun execute(): List<Pair<RecognitionLanguage, LanguageState.Loading>> {
        return coroutineScope {
            val currentJobs = workManager
                .getWorkInfosByTag(LanguageDownloadWorker.WORKER_NAME)
                .runCatching {
                    get()
                }.getOrDefault(emptyList())
                .filter { it.state.isFinished.not() }


            RecognitionLanguage.values()
                .mapNotNull { language ->
                    (currentJobs.firstOrNull { it.tags.contains(language.uniqueWorkName()) }?.progress
                        ?.getInt(PROGRESS, 0)?.let { language to LanguageState.Loading(it) })
                }
        }
    }
}

