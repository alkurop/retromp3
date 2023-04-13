package com.omar.retromp3recorder.io.language

import com.omar.retromp3recorder.domain.VoskRecognitionLanguage

interface LanguageLister {
    suspend fun listAvailableRecognitionLanguages(): List<VoskRecognitionLanguage>
}
