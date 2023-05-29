package com.omar.retromp3recorder.bl.audio.speech

import com.omar.retromp3recorder.domain.LanguageAvailability
import com.omar.retromp3recorder.io.speech.downloader.CheckLanguagesInDownload
import com.omar.retromp3recorder.io.language.LanguageLister
import com.omar.retromp3recorder.storage.repo.global.LanguageAvailabilityRepo
import javax.inject.Inject

class AvailableLanguageListUC @Inject constructor(
    private val repo: LanguageAvailabilityRepo,
    private val languageLister: LanguageLister,
    private val checkDownloadStatusHook: CheckLanguagesInDownload
) {
    suspend fun execute() {
        val languages = languageLister.listAvailableRecognitionLanguages()
        val inDownload = checkDownloadStatusHook.execute()
        val result = languages
            .filter { item ->
                inDownload.map { it.first }.contains(item.language).not()
            }
            .plus(inDownload.map { LanguageAvailability(it.first, it.second) })
            .sortedBy { it.language }
        repo.emit(result)
    }
}
