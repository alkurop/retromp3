package com.omar.retromp3recorder.io.language

import com.omar.retromp3recorder.domain.LanguageAvailability
import com.omar.retromp3recorder.domain.LanguageState
import com.omar.retromp3recorder.domain.RecognitionLanguage
import javax.inject.Inject

internal class VoskLanguageLister @Inject constructor() : LanguageLister {
    override suspend fun listAvailableRecognitionLanguages(): List<LanguageAvailability> {
        return RecognitionLanguage.values()
            .map { LanguageAvailability(it, LanguageState.ToDownload) }
    }
}
