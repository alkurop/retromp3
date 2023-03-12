package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.database.DbUpdaterUC
import com.omar.retromp3recorder.bl.files.FileRepoUpdaterUC
import com.omar.retromp3recorder.bl.waveform.WaveformScanner
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.isEmpty
import com.omar.retromp3recorder.utils.domain.AmplitudaDealer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class WaveformScanUpdaterUC @Inject constructor(
    private val amplitudaDealer: AmplitudaDealer,
    private val dbUpdaterUC: DbUpdaterUC,
    private val fileRepoUpdaterUC: FileRepoUpdaterUC,
    private val waveformScanner: WaveformScanner,
    private val scheduler: Scheduler
) {
    fun execute(input: List<ExistingFileWrapper>): Completable {
        val batch = input.filter { it.wavetable.isEmpty() }
        if (batch.isEmpty()) return Completable.complete()

        val mapIndexed = batch.map { existingFileWrapper ->
            waveformScanner
                .execute(
                    existingFileWrapper,
                    amplitudaDealer.createAmplituda()
                )
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
            .subscribeOn(scheduler)
    }
}
