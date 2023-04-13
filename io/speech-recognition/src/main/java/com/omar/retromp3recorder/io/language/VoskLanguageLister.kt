package com.omar.retromp3recorder.io.language

import com.omar.retromp3recorder.domain.VoskRecognitionLanguage
import javax.inject.Inject

internal class VoskLanguageLister @Inject constructor() : LanguageLister {
    override suspend fun listAvailableRecognitionLanguages(): List<VoskRecognitionLanguage> {
        return emptyList()
    }
}
