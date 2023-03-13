package com.omar.retromp3recorder.app.screens.settings.components.sample_rate

import com.omar.retromp3recorder.bl.settings.ChangeSampleRateUC
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

class SampleRateInteractorFlow @Inject constructor(
    private val changeSampleRateUC: ChangeSampleRateUC,
    private val recorderPrefsRepo: RecorderPrefsRepo,
    dispatcher: CoroutineDispatcher,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    fun processIO(upstream: Flow<Mp3VoiceRecorder.SampleRate>): Flow<Mp3VoiceRecorder.SampleRate> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge()
    }

    private fun Flow<Mp3VoiceRecorder.SampleRate>.processInputs(): Flow<Mp3VoiceRecorder.SampleRate> {
        return this.transform { event ->
            launch {
                changeSampleRateUC.execute(event)
            }
        }
    }

    private fun listenToRepos(): Flow<Mp3VoiceRecorder.SampleRate> {
        return listOf(
            recorderPrefsRepo.observeFlow().map { it.sampleRate }
        ).merge()
    }
}
