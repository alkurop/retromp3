package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.PlayButtonStateMapper
import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.RecordButtonStateMapper
import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.ShareButtonStateMapper
import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.StopButtonStateMapper
import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.bl.audio.StartPlaybackUC
import com.omar.retromp3recorder.bl.audio.StartRecordUC
import com.omar.retromp3recorder.bl.audio.StopPlaybackAndRecordUC
import com.omar.retromp3recorder.bl.system.ShareUC
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.utils.mapToUsecase
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class AudioControlsInteractor @Inject constructor(
    private val playButtonStateMapper: PlayButtonStateMapper,
    private val joinedProgressMapper: JoinedProgressMapper,
    private val recordButtonStateMapper: RecordButtonStateMapper,
    private val recorderDurationStateMapper: RecorderDurationStateMapper,
    private val shareButtonStateMapper: ShareButtonStateMapper,
    private val stopButtonStateMapper: StopButtonStateMapper,
    private val startRecordUC: StartRecordUC,
    private val shareUC: ShareUC,
    private val startPlaybackUC: StartPlaybackUC,
    private val stopPlaybackAndRecordUC: StopPlaybackAndRecordUC,
    private val workScheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<AudioControlsView.Input, AudioControlsView.Output> =
        workScheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<AudioControlsView.Output> = {
        Observable.merge(
            listOf(
                playButtonStateMapper.observe()
                    .map { AudioControlsView.Output.PlayButtonState(it) },
                joinedProgressMapper.observe()
                    .map {
                        val progress = (it as? JoinedProgress.PlayerProgressShown)?.progress
                        AudioControlsView.Output.PlayerProgressState(progress?.toProgressDisplay())
                    },
                recordButtonStateMapper.observe()
                    .map { AudioControlsView.Output.RecordButtonState(it) },
                recorderDurationStateMapper.observe(),
                shareButtonStateMapper.observe()
                    .map { AudioControlsView.Output.ShareButtonState(it) },
                stopButtonStateMapper.observe()
                    .map { AudioControlsView.Output.StopButtonState(it) }
            )
        )
    }
    private val mapInputToUsecase: (Observable<AudioControlsView.Input>) -> Completable = { input ->
        Completable.merge(
            listOf(
                input.mapToUsecase<AudioControlsView.Input.Play> { startPlaybackUC.execute() },
                input.mapToUsecase<AudioControlsView.Input.Record> { startRecordUC.execute() },
                input.mapToUsecase<AudioControlsView.Input.Share> { shareUC.execute() },
                input.mapToUsecase<AudioControlsView.Input.Stop> { stopPlaybackAndRecordUC.execute() },
            )
        )
    }
}



