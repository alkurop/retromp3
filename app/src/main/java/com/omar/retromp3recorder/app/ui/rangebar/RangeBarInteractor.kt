package com.omar.retromp3recorder.app.ui.rangebar

import com.omar.retromp3recorder.bl.audio.UpdatePlayerRangeUC
import com.omar.retromp3recorder.bl.settings.ActivateRangeUC
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class RangeBarInteractor @Inject constructor(
    private val rangeStateMapper: RangeBarStateMapper,
    private val updatePlayerRangeUC: UpdatePlayerRangeUC,
    private val rangeEnableRangeUC: ActivateRangeUC,
    private val workScheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<RangeBarView.Input, RangeBarView.State> =
        workScheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<RangeBarView.State> = {
        Observable.merge(
            listOf(
                rangeStateMapper.observe()
            )
        )
    }
    private val mapInputToUsecase: (Observable<RangeBarView.Input>) -> Completable = { input ->
        Completable.merge(
            listOf(
                input.ofType(RangeBarView.Input.RangeSet::class.java).flatMapCompletable {
                    updatePlayerRangeUC.execute(it.range)
                },
                input.ofType(RangeBarView.Input.Enable::class.java).flatMapCompletable {
                    rangeEnableRangeUC.execute()
                }
            )
        )
    }
}
