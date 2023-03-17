package com.omar.retromp3recorder.app.screens.settings.components.sample_rate

import com.omar.retromp3recorder.bl.settings.ChangeSampleRateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class SampleRateInteractorFlow @Inject constructor(
    private val changeSampleRateUC: ChangeSampleRateUC,
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val dispatcher: CoroutineDispatcher,
) {

    fun processIO(upstream: Flow<Mp3VoiceRecorder.SampleRate>): Flow<Mp3VoiceRecorder.SampleRate> {
        return listOf(
            listenToRepos(),
            upstream.processInputs(),
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<Mp3VoiceRecorder.SampleRate>.processInputs(): Flow<Mp3VoiceRecorder.SampleRate> {
        return this.flatMapMerge { event ->
            flow {
                changeSampleRateUC.execute(event)
            }
        }
    }

    private fun listenToRepos(): Flow<Mp3VoiceRecorder.SampleRate> {
        return listOf(
            recorderPrefsRepo.flow().map { it.sampleRate }
        ).merge()
    }
}
