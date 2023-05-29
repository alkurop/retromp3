package com.omar.retromp3recorder.app.screens.home.components.language.popup

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.LanguageAvailability
import com.omar.retromp3recorder.domain.RecognitionLanguage

interface SpeechPopupContract {
    sealed interface Input {
        data class DeleteLanguage(val language: RecognitionLanguage) : Input
        data class DownloadLanguage(val language: RecognitionLanguage) : Input
        data class CancelDownload(val language: RecognitionLanguage) : Input
    }

    @Immutable
    data class State(
        val languages: List<LanguageAvailability> = emptyList()
    )
}


