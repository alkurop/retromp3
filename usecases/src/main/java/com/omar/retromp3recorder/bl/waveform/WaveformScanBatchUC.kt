package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.Loading
import com.omar.retromp3recorder.storage.repo.LoadingRepo
import com.omar.retromp3recorder.utils.AmplitudaDealer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WaveformScanBatchUC @Inject constructor(
    private val amplitudaDealer: AmplitudaDealer,
    private val waveformScanner: WaveformScanner,
    private val loadingRepo: LoadingRepo,
    private val scheduler: Scheduler
) {
    fun execute(batch: List<ExistingFileWrapper>): Completable {
        if (batch.isEmpty()) return Completable.complete()

        val percentInOne = (100 / batch.size)
        return Single
            .zip(
                batch.mapIndexed { index, existingFileWrapper ->
                    waveformScanner.execute(
                        existingFileWrapper,
                        amplitudaDealer.createAmplituda()
                    ).doAfterSuccess {
                        loadingRepo.onNext(Loading.Is((index + 1) * percentInOne))
                    }
                }
            ) { array ->
                array.map { it as ExistingFileWrapper }
            }.flatMapCompletable {
                Completable.complete()
            }
            .subscribeOn(scheduler)
            .doOnSubscribe { loadingRepo.onNext(Loading.Is(0)) }
            .doOnComplete { loadingRepo.onNext(Loading.Not) }
    }
}
