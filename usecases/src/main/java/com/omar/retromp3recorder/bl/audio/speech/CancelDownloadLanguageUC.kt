package com.omar.retromp3recorder.bl.audio.speech

import com.omar.retromp3recorder.domain.RecognitionLanguage
import com.omar.retromp3recorder.storage.repo.global.LanguageRecognitionRepo
import javax.inject.Inject

class CancelDownloadLanguageUC @Inject constructor(
    private val repo: LanguageRecognitionRepo
) {
    suspend fun execute(language: RecognitionLanguage) {}
}
