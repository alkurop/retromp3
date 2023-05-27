package com.omar.retromp3recorder.io.language

import com.omar.retromp3recorder.domain.LanguageAvailability
import com.omar.retromp3recorder.domain.LanguageState
import com.omar.retromp3recorder.domain.RecognitionLanguage
import com.omar.retromp3recorder.utils.platform.DirPathProvider
import java.io.File
import javax.inject.Inject

internal class VoskLanguageLister @Inject constructor(
    private val dirPathProvider: DirPathProvider,
) :
    LanguageLister {
    override suspend fun listAvailableRecognitionLanguages(): List<LanguageAvailability> {
        val languages = RecognitionLanguage.values()
        val modelPath = dirPathProvider.provideModelDirPath()
        return languages.map { item -> item to "$modelPath/${item.getFilename()}" }
            .map { (item, path) ->
                val available = File(path).runCatching {
                    this.exists()
                }.getOrDefault(false)
                if (available) {
                    LanguageAvailability(item, LanguageState.Available)
                } else {
                    LanguageAvailability(item, LanguageState.ToDownload)
                }
            }
    }
}
