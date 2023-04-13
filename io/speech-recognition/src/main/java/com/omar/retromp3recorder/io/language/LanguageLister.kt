package com.omar.retromp3recorder.io.language

import com.omar.retromp3recorder.domain.LanguageAvailability

interface LanguageLister {
    suspend fun listAvailableRecognitionLanguages(): List<LanguageAvailability>
}
