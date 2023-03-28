package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.PlayButtonStateFlow
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.RecordButtonStateFlow
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.ShareButtonStateFlow
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.StopButtonStateMapperFlow
import com.omar.retromp3recorder.bl.audio.actions.StartPlaybackUC
import com.omar.retromp3recorder.bl.audio.actions.StartRecordUC
import com.omar.retromp3recorder.bl.audio.actions.StopPlaybackAndRecordUC
import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
import com.omar.retromp3recorder.bl.system.ShareUC
import com.omar.retromp3recorder.domain.JoinedProgress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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
    dispatcher: CoroutineDispatcher,
) : Interactor<AudioControlsView.Input, AudioControlsView.Output>(dispatcher) {

    override fun listRepos(): List<Flow<AudioControlsView.Output>> {
        return listOf(
            playButtonStateMapper.flow().map { AudioControlsView.Output.PlayButtonState(it) },
            recordButtonStateMapper.flow().map { AudioControlsView.Output.RecordButtonState(it) },
            shareButtonStateMapper.flow().map { AudioControlsView.Output.ShareButtonState(it) },
            stopButtonStateMapper.flow().map { AudioControlsView.Output.StopButtonState(it) },
            recorderDurationStateFlow.flow(),
            joinedProgressMapper.flow().map {
                val progress = (it as? JoinedProgress.PlayerProgressShown)?.progress
                AudioControlsView.Output.PlayerProgressState(progress)
            },
        )
    }

    override suspend fun FlowCollector<AudioControlsView.Output>.launchUseCase(input: AudioControlsView.Input) {
        when (input) {
            AudioControlsView.Input.Play -> startPlaybackUC.execute()
            AudioControlsView.Input.Record -> startRecordUC.execute()
            AudioControlsView.Input.Share -> shareUC.execute()
            AudioControlsView.Input.Stop -> stopPlaybackAndRecordUC.execute()
        }
    }
}



