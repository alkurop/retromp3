package com.omar.retromp3recorder.bl.audio.speech

import com.omar.retromp3recorder.io.language.LanguageLister
import com.omar.retromp3recorder.storage.repo.local.LanguageRecognitionRepo
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AvailableLanguageListUC @Inject constructor(
    private val repo: LanguageRecognitionRepo,
    private val languageLister: LanguageLister,
    private val jobWrapper: ScopeJobWrapper
) {
    suspend fun execute() {
        val languages =
            withContext(jobWrapper.coroutineContext) { languageLister.listAvailableRecognitionLanguages() }
        repo.emit(languages)
    }
}
