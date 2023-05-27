package com.omar.retromp3recorder.utils.platform


interface LanguageDownloadNotificationSender {
    fun sendNotification(status: LanguageDownloadStatus)
}

sealed interface LanguageDownloadStatus {
    val language: String

    data class Progress(override val language: String, val percent: Int) : LanguageDownloadStatus
    data class Finished(override val language: String) : LanguageDownloadStatus
}
