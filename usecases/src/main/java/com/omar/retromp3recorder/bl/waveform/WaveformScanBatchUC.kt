package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.bl.files.DbUpdaterUC
import com.omar.retromp3recorder.bl.files.FileRepoUpdaterUC
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.isEmpty
import com.omar.retromp3recorder.storage.repo.Loading
import com.omar.retromp3recorder.storage.repo.LoadingRepo
import com.omar.retromp3recorder.utils.AmplitudaDealer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class WaveformScanBatchUC @Inject constructor(
    private val amplitudaDealer: AmplitudaDealer,
    private val waveformScanner: WaveformScanner,
    private val loadingRepo: LoadingRepo,
    private val scheduler: Scheduler,
    private val dbUpdaterUC: DbUpdaterUC,
    private val fileRepoUpdaterUC: FileRepoUpdaterUC
) {
    fun execute(input: List<ExistingFileWrapper>): Completable {
        val batch = input.filter { it.wavetable.isEmpty() }
        if (batch.isEmpty()) return Completable.complete()

        val percentInOne = (100 / batch.size)
        return Completable
            .concat(
                batch.mapIndexed { index, existingFileWrapper ->
                    waveformScanner
                        .execute(
                            existingFileWrapper,
                            amplitudaDealer.createAmplituda()
                        )
                        .doAfterSuccess {
                            loadingRepo.onNext(Loading.Is((index + 1) * percentInOne))
                        }
                        .flatMapCompletable {

                            Completable.merge(
                                listOf(
                                    fileRepoUpdaterUC.execute(listOf(it)),
                                    dbUpdaterUC.execute(listOf(it))
                                )
                            )
                        }
                }
            )
            .doOnSubscribe { loadingRepo.onNext(Loading.Is(0)) }
            .doOnComplete { loadingRepo.onNext(Loading.Not) }
            .subscribeOn(scheduler)
    }
}
