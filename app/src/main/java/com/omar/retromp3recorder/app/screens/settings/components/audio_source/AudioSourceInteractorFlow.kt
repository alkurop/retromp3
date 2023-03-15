package com.omar.retromp3recorder.app.screens.settings.components.audio_source

import com.omar.retromp3recorder.bl.settings.ChangeAudioSourceUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class AudioSourceInteractorFlow @Inject constructor(
    private val changeAudioSourceUC: ChangeAudioSourceUC,
    private val repo: RecorderPrefsRepo,
    private val dispatcher: CoroutineDispatcher
) {

    fun processIO(upstream: Flow<Mp3VoiceRecorder.AudioSourcePref>): Flow<Mp3VoiceRecorder.AudioSourcePref> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<Mp3VoiceRecorder.AudioSourcePref>.processInputs(): Flow<Mp3VoiceRecorder.AudioSourcePref> {
        return this.flatMapMerge { event ->
            flow {
                changeAudioSourceUC.execute(event)
            }
        }
    }

    private fun listenToRepos(): Flow<Mp3VoiceRecorder.AudioSourcePref> {
        return listOf(
            repo.flow().map { it.audioSourcePref }
        ).merge()
    }
}
