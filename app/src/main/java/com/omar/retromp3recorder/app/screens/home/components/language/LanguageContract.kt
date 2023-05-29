package com.omar.retromp3recorder.app.screens.home.components.language

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.RecognitionLanguage

object LanguageContract {
    sealed interface Input {
        data class SelectLanguage(val language: RecognitionLanguage) : Input
    }

    sealed interface Output {
        data class Visibility(val isVisible: Boolean) : Output
        data class Availability(val availableLanguages: List<RecognitionLanguage>) : Output
    }

    @Immutable
    data class State(
        val isVisible: Boolean = false,
        val selectedLanguage: RecognitionLanguage? = null,
        val availableLanguages: List<RecognitionLanguage> = emptyList()
    )
}
