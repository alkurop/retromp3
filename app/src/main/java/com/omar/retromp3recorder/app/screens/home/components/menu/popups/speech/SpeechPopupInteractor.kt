package com.omar.retromp3recorder.app.screens.home.components.menu.popups.speech

import com.omar.retromp3recorder.app.Interactor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class SpeechPopupInteractor @Inject constructor(
    dispatcher: CoroutineDispatcher
) : Interactor<SpeechPopupContract.Input, SpeechPopupContract.State>(dispatcher) {
    override fun listRepos(): List<Flow<SpeechPopupContract.State>> {
        TODO("Not yet implemented")
    }

    override suspend fun FlowCollector<SpeechPopupContract.State>.launchUseCase(input: SpeechPopupContract.Input) {
        TODO("Not yet implemented")
    }
}
