package com.omar.retromp3recorder.app.screens.home.components.language

import androidx.compose.runtime.Stable

object LanguageContract {
    sealed interface Input {}
    sealed interface Output {
        data class Visibility(val isVisible: Boolean) : Output
    }

    @Stable
    data class State(val isVisible: Boolean = false)
}
