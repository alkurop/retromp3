package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.database.DbUpdaterUC
import com.omar.retromp3recorder.bl.files.FileRepoUpdaterUC
import com.omar.retromp3recorder.bl.waveform.WaveformScanner
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.isEmpty
import com.omar.retromp3recorder.storage.repo.Loading
import com.omar.retromp3recorder.storage.repo.LoadingStateRepo
import com.omar.retromp3recorder.utils.AmplitudaDealer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class WaveformScanUpdaterUC @Inject constructor(
    private val amplitudaDealer: AmplitudaDealer,
    private val waveformScanner: WaveformScanner,
    private val loadingStateRepo: LoadingStateRepo,
    private val scheduler: Scheduler,
    private val dbUpdaterUC: DbUpdaterUC,
    private val fileRepoUpdaterUC: FileRepoUpdaterUC
) {
    fun execute(input: List<ExistingFileWrapper>): Completable {
        val batch = input.filter { it.wavetable.isEmpty() }
        if (batch.isEmpty()) return Completable.complete()

        val percentInOne = (100 / batch.size)
        val mapIndexed = batch.mapIndexed { index, existingFileWrapper ->
            waveformScanner
                .execute(
                    existingFileWrapper,
                    amplitudaDealer.createAmplituda()
                )
                .doAfterSuccess {
                    loadingStateRepo.onNext(Loading.Is((index + 1) * percentInOne))
                }
                .flatMapCompletable {
                    Completable.merge(
                        listOf(
                            dbUpdaterUC.execute(listOf(it)),
                            fileRepoUpdaterUC.execute(listOf(it))
                        )
                    )
                }
        }
        return Completable
            .merge(mapIndexed)
            .doOnSubscribe { loadingStateRepo.onNext(Loading.Is(0)) }
            .doOnComplete { loadingStateRepo.onNext(Loading.Not) }
            .subscribeOn(scheduler)
    }
}
