package com.omar.retromp3recorder.app.screens.home.components.language

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LanguageVisibilityMapper @Inject constructor(
    private val controlsRepo: PlayerControlsRepo
) {
    fun execute(): Flow<Boolean> {
        return controlsRepo.flow().map { controls ->
            controls.speechSettings.isVisible
        }
    }
}
