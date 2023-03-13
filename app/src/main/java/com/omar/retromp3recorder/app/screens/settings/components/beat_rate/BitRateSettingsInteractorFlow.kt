package com.omar.retromp3recorder.app.screens.settings.components.beat_rate

import com.omar.retromp3recorder.bl.settings.ChangeBitrateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class BitRateSettingsInteractorFlow @Inject constructor(
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val changeBitrateUC: ChangeBitrateUC,
     dispatcher: CoroutineDispatcher,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    fun processIO(upstream: Flow<Mp3VoiceRecorder.BitRate>): Flow<Mp3VoiceRecorder.BitRate> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge()
    }

    private fun Flow<Mp3VoiceRecorder.BitRate>.processInputs(): Flow<Mp3VoiceRecorder.BitRate> {
        return this.transform { event ->
            launch {
                changeBitrateUC.execute(event)
            }
        }
    }

    private fun listenToRepos(): Flow<Mp3VoiceRecorder.BitRate> {
        return listOf(
            recorderPrefsRepo.flow().map { it.bitRate }
        ).merge()
    }
}
