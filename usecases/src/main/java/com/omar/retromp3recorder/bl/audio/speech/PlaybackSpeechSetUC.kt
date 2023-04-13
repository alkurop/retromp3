package com.omar.retromp3recorder.bl.audio.speech

import com.omar.retromp3recorder.domain.SpeechRecognitionLanguage
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import javax.inject.Inject

class PlaybackSpeechSetLanguageUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute(language: SpeechRecognitionLanguage) {
        val currentSettings = playerControlsRepo.first()
        val speechSettings = currentSettings.speechSettings.copy(language = language)
        playerControlsRepo.emit(currentSettings.copy(speechSettings = speechSettings))
    }
}

class PlaybackSpeechEnabledUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute() {
        val currentSettings = playerControlsRepo.first()
        val isEnabled = currentSettings.speechSettings.isEnabled.not()
        val speechSettings = currentSettings.speechSettings.copy(isEnabled = isEnabled)
        playerControlsRepo.emit(currentSettings.copy(speechSettings = speechSettings))
    }
}

class PlaybackSpeechVisibleUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute(isVisible: Boolean) {
        val currentSettings = playerControlsRepo.first()
        val speechSettings = currentSettings.speechSettings.copy(isVisible = isVisible)
        playerControlsRepo.emit(currentSettings.copy(speechSettings = speechSettings))
    }
}

