package com.omar.retromp3recorder.bl.audio.speech

import com.omar.retromp3recorder.domain.LanguageAvailability
import com.omar.retromp3recorder.domain.LanguageState
import com.omar.retromp3recorder.domain.RecognitionLanguage
import com.omar.retromp3recorder.io.language.getFilename
import com.omar.retromp3recorder.storage.repo.global.LanguageAvailabilityRepo
import com.omar.retromp3recorder.utils.platform.DirPathProvider
import java.io.File
import javax.inject.Inject

class DeleteLanguageUC @Inject constructor(
    private val repo: LanguageAvailabilityRepo,
    private val dirPathProvider: DirPathProvider,
) {
    suspend fun execute(recognitionLanguage: RecognitionLanguage) {
        val name = recognitionLanguage.getFilename()
        val path = dirPathProvider.provideModelDirPath()
        File("$path/$name").runCatching { delete() }
        repo.updateItem(LanguageAvailability(recognitionLanguage, LanguageState.ToDownload(false)))
    }
}
