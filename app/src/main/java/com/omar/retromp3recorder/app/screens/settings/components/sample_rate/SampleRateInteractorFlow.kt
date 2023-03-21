package com.omar.retromp3recorder.app.screens.settings.components.sample_rate

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.settings.ChangeSampleRateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SampleRateInteractorFlow @Inject constructor(
    private val changeSampleRateUC: ChangeSampleRateUC,
    private val recorderPrefsRepo: RecorderPrefsRepo,
    dispatcher: CoroutineDispatcher,
) : Interactor<Mp3VoiceRecorder.SampleRate, Mp3VoiceRecorder.SampleRate>(dispatcher) {

    override fun listRepos(): List<Flow<Mp3VoiceRecorder.SampleRate>> {
        return listOf(recorderPrefsRepo.flow().map { it.sampleRate })
    }

    override suspend fun FlowCollector<Mp3VoiceRecorder.SampleRate>.launchUseCase(input: Mp3VoiceRecorder.SampleRate) {
        changeSampleRateUC.execute(input)
    }
}
