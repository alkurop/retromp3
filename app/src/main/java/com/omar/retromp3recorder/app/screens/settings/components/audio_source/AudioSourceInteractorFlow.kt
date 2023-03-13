package com.omar.retromp3recorder.app.screens.settings.components.audio_source

import com.omar.retromp3recorder.bl.settings.ChangeAudioSourceUC
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

class AudioSourceInteractorFlow @Inject constructor(
    private val repo: RecorderPrefsRepo,
    private val changeAudioSourceUC: ChangeAudioSourceUC,
    dispatcher: CoroutineDispatcher
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    fun processIO(upstream: Flow<Mp3VoiceRecorder.AudioSourcePref>): Flow<Mp3VoiceRecorder.AudioSourcePref> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge()
    }

    private fun Flow<Mp3VoiceRecorder.AudioSourcePref>.processInputs(): Flow<Mp3VoiceRecorder.AudioSourcePref> {
        return this.transform { event ->
            launch {
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
