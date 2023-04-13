package com.omar.retromp3recorder.app.screens.home.components.menu.popups.speech

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.speech.CancelDownloadLanguageUC
import com.omar.retromp3recorder.bl.audio.speech.DeleteLanguageUC
import com.omar.retromp3recorder.bl.audio.speech.DownloadLanguageUC
import com.omar.retromp3recorder.storage.repo.global.LanguageRecognitionRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SpeechPopupInteractor @Inject constructor(
    private val languageRecognitionRepo: LanguageRecognitionRepo,
    private val downloadLanguageUC:DownloadLanguageUC,
    private val deleteLanguageUC: DeleteLanguageUC,
    private val cancelDownloadLanguageUC: CancelDownloadLanguageUC,
    dispatcher: CoroutineDispatcher
) : Interactor<SpeechPopupContract.Input, SpeechPopupContract.State>(dispatcher) {

    override fun listRepos(): List<Flow<SpeechPopupContract.State>> {
        return listOf(languageRecognitionRepo.flow().map { SpeechPopupContract.State(it) })
    }

    override suspend fun FlowCollector<SpeechPopupContract.State>.launchUseCase(input: SpeechPopupContract.Input) {
       when(input){
           is SpeechPopupContract.Input.DownloadLanguage -> downloadLanguageUC.execute(input.language)
           is SpeechPopupContract.Input.CancelDownload -> cancelDownloadLanguageUC.execute(input.language)
           is SpeechPopupContract.Input.DeleteLanguage -> deleteLanguageUC.execute(input.language)
       }
    }
}
