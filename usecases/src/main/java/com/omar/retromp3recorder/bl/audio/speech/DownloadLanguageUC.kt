package com.omar.retromp3recorder.bl.audio.speech

import com.omar.retromp3recorder.domain.LanguageState
import com.omar.retromp3recorder.domain.RecognitionLanguage
import com.omar.retromp3recorder.io.speech.downloader.DownloadLanguageHook
import com.omar.retromp3recorder.storage.repo.global.LanguageAvailabilityRepo
import javax.inject.Inject

class DownloadLanguageUC @Inject constructor(
    private val repo: LanguageAvailabilityRepo,
    private val downloadLanguageHook: DownloadLanguageHook
) {
    suspend fun execute(language: RecognitionLanguage) {
        val currentState = repo.first().first { it.language == language }.state
        if (currentState is LanguageState.ToDownload) {
            downloadLanguageHook.execute(language)
        }
    }
}
