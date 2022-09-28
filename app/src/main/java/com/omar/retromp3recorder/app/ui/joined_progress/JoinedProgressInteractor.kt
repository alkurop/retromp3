package com.omar.retromp3recorder.app.ui.joined_progress

import com.omar.retromp3recorder.bl.audio.AudioSeekFinishUC
import com.omar.retromp3recorder.bl.audio.AudioSeekPauseUC
import com.omar.retromp3recorder.bl.audio.AudioSeekProgressUC
import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.utils.mapToUsecase
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class JoinedProgressInteractor @Inject constructor(
    private val audioSeekProgressUC: AudioSeekProgressUC,
    private val audioSeekPauseUC: AudioSeekPauseUC,
    private val audioSeekFinishUC: AudioSeekFinishUC,
    private val joinedProgressRepo: JoinedProgressMapper,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<JoinedProgressView.In, JoinedProgress> =
        scheduler.processIO(
            inputMapper = inputMapper,
            outputMapper = mapRepoToOutput,
        )

    private val mapRepoToOutput: () -> Observable<JoinedProgress> = {
        joinedProgressRepo.observe()
    }
    private val inputMapper: (Observable<JoinedProgressView.In>) -> Completable = { input ->
        Completable.merge(listOf(
            input.mapToUsecase<JoinedProgressView.In.SeekToPosition> {
                audioSeekProgressUC.execute(it.position)
            },
            input.mapToUsecase<JoinedProgressView.In.SeekingStarted> { audioSeekPauseUC.execute() },
            input.mapToUsecase<JoinedProgressView.In.SeekingFinished> { audioSeekFinishUC.execute() }
        ))
    }
}
