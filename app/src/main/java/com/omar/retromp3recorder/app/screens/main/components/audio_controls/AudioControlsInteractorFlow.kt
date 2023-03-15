package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.PlayButtonStateFlow
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.RecordButtonStateFlow
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.ShareButtonStateFlow
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.StopButtonStateMapperFlow
import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.bl.audio.StartPlaybackUC
import com.omar.retromp3recorder.bl.audio.StartRecordUC
import com.omar.retromp3recorder.bl.audio.StopPlaybackAndRecordUC
import com.omar.retromp3recorder.bl.system.ShareUC
import com.omar.retromp3recorder.domain.JoinedProgress
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject

@OptIn(FlowPreview::class)
class AudioControlsInteractorFlow @Inject constructor(
    private val playButtonStateMapper: PlayButtonStateFlow,
    private val joinedProgressMapper: JoinedProgressMapper,
    private val recordButtonStateMapper: RecordButtonStateFlow,
    private val recorderDurationStateFlow: RecorderDurationStateFlow,
    private val shareButtonStateMapper: ShareButtonStateFlow,
    private val stopButtonStateMapper: StopButtonStateMapperFlow,
    private val startRecordUC: StartRecordUC,
    private val shareUC: ShareUC,
    private val startPlaybackUC: StartPlaybackUC,
    private val stopPlaybackAndRecordUC: StopPlaybackAndRecordUC,
    private val dispatcher: CoroutineDispatcher,
) {
    fun processIO(upstream: Flow<AudioControlsView.Input>): Flow<AudioControlsView.Output> {
        return listOf(
            upstream.processInputs().flowOn(dispatcher),
            listenToRepos()
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<AudioControlsView.Input>.processInputs(): Flow<AudioControlsView.Output> {
        return this.flatMapMerge { event ->
            flow {
                when (event) {
                    AudioControlsView.Input.Play -> {
                        startPlaybackUC.execute().blockingAwait()
                    }
                    AudioControlsView.Input.Record -> {
                        startRecordUC.execute().blockingAwait()
                    }
                    AudioControlsView.Input.Share -> {
                        shareUC.execute().blockingAwait()
                    }
                    AudioControlsView.Input.Stop -> {
                        stopPlaybackAndRecordUC.execute().blockingAwait()
                    }
                }
            }
        }
    }

    private fun listenToRepos(): Flow<AudioControlsView.Output> {
        return listOf(
            playButtonStateMapper.flow()
                .map { AudioControlsView.Output.PlayButtonState(it) },
            recordButtonStateMapper.flow()
                .map { AudioControlsView.Output.RecordButtonState(it) },
            shareButtonStateMapper.flow()
                .map { AudioControlsView.Output.ShareButtonState(it) },
            stopButtonStateMapper.flow()
                .map { AudioControlsView.Output.StopButtonState(it) },
            recorderDurationStateFlow.flow(),
            joinedProgressMapper.observe().asFlow()
                .map {
                    val progress = (it as? JoinedProgress.PlayerProgressShown)?.progress
                    AudioControlsView.Output.PlayerProgressState(progress)
                },
        ).merge()
    }
}



