package com.omar.retromp3recorder.app.screens.settings.components.beat_rate

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.settings.ChangeBitrateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BitRateSettingsInteractorFlow @Inject constructor(
    private val changeBitrateUC: ChangeBitrateUC,
    private val recorderPrefsRepo: RecorderPrefsRepo,
    dispatcher: CoroutineDispatcher,
) : Interactor<Mp3VoiceRecorder.BitRate, Mp3VoiceRecorder.BitRate>(dispatcher) {

    override fun listRepos(): List<Flow<Mp3VoiceRecorder.BitRate>> {
        return listOf(recorderPrefsRepo.flow().map { it.bitRate })
    }

    override suspend fun FlowCollector<Mp3VoiceRecorder.BitRate>.launchUseCase(input: Mp3VoiceRecorder.BitRate) {
        changeBitrateUC.execute(input)
    }
}
