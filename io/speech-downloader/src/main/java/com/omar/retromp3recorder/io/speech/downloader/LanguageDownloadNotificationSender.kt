package com.omar.retromp3recorder.io.speech.downloader

import com.omar.retromp3recorder.domain.RecognitionLanguage


interface LanguageDownloadNotificationSender {
    suspend fun sendNotification(status: LanguageDownloadStatus)
}

sealed interface LanguageDownloadStatus {
    val language: RecognitionLanguage

    data class LoadingProgress(
        override val language: RecognitionLanguage,
        val percent: Int
    ) : LanguageDownloadStatus

    data class InstallingProgress(
        override val language: RecognitionLanguage,
        val percent: Int
    ) : LanguageDownloadStatus

    data class FinishedSuccess(
        override val language: RecognitionLanguage
    ) : LanguageDownloadStatus

    data class FinishedWithError(
        override val language: RecognitionLanguage,
        val cause: Throwable
    ) : LanguageDownloadStatus
}

