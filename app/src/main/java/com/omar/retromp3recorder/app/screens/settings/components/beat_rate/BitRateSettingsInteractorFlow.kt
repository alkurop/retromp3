package com.omar.retromp3recorder.app.screens.settings.components.beat_rate

import com.omar.retromp3recorder.bl.settings.ChangeBitrateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BitRateSettingsInteractorFlow @Inject constructor(
    private val changeBitrateUC: ChangeBitrateUC,
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val dispatcher: CoroutineDispatcher,
) {
    fun processIO(upstream: Flow<Mp3VoiceRecorder.BitRate>): Flow<Mp3VoiceRecorder.BitRate> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<Mp3VoiceRecorder.BitRate>.processInputs(): Flow<Mp3VoiceRecorder.BitRate> {
        return this.transform { event ->
            changeBitrateUC.execute(event)
        }
    }

    private fun listenToRepos(): Flow<Mp3VoiceRecorder.BitRate> {
        return listOf(
            recorderPrefsRepo.flow().map { it.bitRate }
        ).merge()
    }
}
