package com.omar.retromp3recorder.bl.audio.speech

import com.omar.retromp3recorder.io.language.LanguageLister
import com.omar.retromp3recorder.storage.repo.global.LanguageAvailabilityRepo
import javax.inject.Inject

class AvailableLanguageListUC @Inject constructor(
    private val repo: LanguageAvailabilityRepo,
    private val languageLister: LanguageLister,
) {
    suspend fun execute() {
        val languages = languageLister.listAvailableRecognitionLanguages()
        repo.emit(languages)
    }
}
