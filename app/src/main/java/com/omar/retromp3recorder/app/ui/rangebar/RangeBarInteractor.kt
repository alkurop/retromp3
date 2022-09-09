package com.omar.retromp3recorder.app.ui.rangebar

import com.omar.retromp3recorder.bl.audio.UpdatePlayerRangeUC
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class RangeBarInteractor @Inject constructor(
    private val isStateMapper: RangeBarStateMapper,
    private val updatePlayerRangeUC: UpdatePlayerRangeUC,
    private val workScheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<RangeBarView.Input, RangeBarView.Output> =
        workScheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<RangeBarView.Output> = {
        Observable.merge(
            listOf(
                isStateMapper.observe().map {
                    val isVisible = when (it) {
                        is RangeBarView.State.Hidden -> false
                        is RangeBarView.State.Visible -> true
                    }
                    RangeBarView.Output.Visibility(isVisible)
                },
            )
        )
    }
    private val mapInputToUsecase: (Observable<RangeBarView.Input>) -> Completable = { input ->
        Completable.merge(
            listOf(
                input.ofType(RangeBarView.Input.RangeSet::class.java).flatMapCompletable {
                    updatePlayerRangeUC.execute(it.range)
                }
            )
        )
    }
}