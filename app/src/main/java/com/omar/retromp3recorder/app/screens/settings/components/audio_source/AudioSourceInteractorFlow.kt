package com.omar.retromp3recorder.app.screens.settings.components.audio_source

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.settings.ChangeAudioSourceUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AudioSourceInteractorFlow @Inject constructor(
    private val changeAudioSourceUC: ChangeAudioSourceUC,
    private val repo: RecorderPrefsRepo,
    dispatcher: CoroutineDispatcher
) : Interactor<Mp3VoiceRecorder.AudioSourcePref, Mp3VoiceRecorder.AudioSourcePref>(dispatcher) {

    override fun listRepos(): List<Flow<Mp3VoiceRecorder.AudioSourcePref>> {
        return listOf(
            repo.flow().map { it.audioSourcePref }
        )
    }

    override suspend fun ProducerScope<Mp3VoiceRecorder.AudioSourcePref>.launchUseCase(input: Mp3VoiceRecorder.AudioSourcePref) {
        changeAudioSourceUC.execute(input)
    }

}
