package com.omar.retromp3recorder.bl.audio.speech

import com.omar.retromp3recorder.domain.RecognitionLanguage
import com.omar.retromp3recorder.io.speech.downloader.CancelDownloadLanguageHook
import javax.inject.Inject

class CancelDownloadLanguageUC @Inject constructor(
    private val cancelDownloadLanguageUC: CancelDownloadLanguageHook,
    private val deleteLanguageUC: DeleteLanguageUC

) {
    suspend fun execute(language: RecognitionLanguage) {
        cancelDownloadLanguageUC.execute(language)
        deleteLanguageUC.execute(language)
    }
}
