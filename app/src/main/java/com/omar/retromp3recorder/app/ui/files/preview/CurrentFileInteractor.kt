package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.RecordButtonStateMapper
import com.omar.retromp3recorder.app.ui.files.preview.buttonstate.DeleteFileButtonStateMapper
import com.omar.retromp3recorder.app.ui.files.preview.buttonstate.OpenFileButtonStateMapper
import com.omar.retromp3recorder.app.ui.files.preview.buttonstate.RenameFileButtonStateMapper
import com.omar.retromp3recorder.bl.audio.AudioSeekFinishUC
import com.omar.retromp3recorder.bl.audio.AudioSeekPauseUC
import com.omar.retromp3recorder.bl.audio.AudioSeekProgressUC
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import com.omar.retromp3recorder.utils.mapToUsecase
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class CurrentFileInteractor @Inject constructor(
    private val audioSeekProgressUC: AudioSeekProgressUC,
    private val audioSeekPauseUC: AudioSeekPauseUC,
    private val audioSeekFinishUC: AudioSeekFinishUC,
    private val currentFileRepo: CurrentFileRepo,
    private val deleteFileButtonStateMapper: DeleteFileButtonStateMapper,
    private val openFileButtonStateMapper: OpenFileButtonStateMapper,
    private val recordButtonStateMapper: RecordButtonStateMapper,
    private val renameFileButtonStateMapper: RenameFileButtonStateMapper,
    private val scheduler: Scheduler,
) {
    fun processIO(): ObservableTransformer<CurrentFileView.Input, CurrentFileView.Output> =
        scheduler.processIO(
            inputMapper = inputMapper,
            outputMapper = mapRepoToOutput,
        )

    private val mapRepoToOutput: () -> Observable<CurrentFileView.Output> = {
        Observable.merge(
            listOf(
                renameFileButtonStateMapper.observe().map {
                    CurrentFileView.Output.RenameButtonState(it)
                },
                currentFileRepo.observe().map { file ->
                    CurrentFileView.Output.CurrentFileOutput(
                        file.value
                    )
                },
                deleteFileButtonStateMapper.observe()
                    .map { CurrentFileView.Output.DeleteButtonState(it) },
                openFileButtonStateMapper.observe()
                    .map { CurrentFileView.Output.OpenButtonState(it) },
                recordButtonStateMapper.observe()
                    .map { CurrentFileView.Output.IsRecording(it == InteractiveButton.State.RUNNING) }
            )
        )
    }
    private val inputMapper: (Observable<CurrentFileView.Input>) -> Completable = { input ->
        Completable.merge(listOf(
            input.mapToUsecase<CurrentFileView.Input.SeekToPosition> {
                audioSeekProgressUC.execute(
                    it.position
                )
            },
            input.mapToUsecase<CurrentFileView.Input.SeekingStarted> { audioSeekPauseUC.execute() },
            input.mapToUsecase<CurrentFileView.Input.SeekingFinished> { audioSeekFinishUC.execute() }
        ))
    }
}
