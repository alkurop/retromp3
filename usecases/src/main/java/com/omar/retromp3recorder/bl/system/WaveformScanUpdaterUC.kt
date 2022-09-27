package com.omar.retromp3recorder.bl.system

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.bl.R
import com.omar.retromp3recorder.bl.database.DbUpdaterUC
import com.omar.retromp3recorder.bl.files.FileRepoUpdaterUC
import com.omar.retromp3recorder.bl.waveform.WaveformScanner
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.LogEvent
import com.omar.retromp3recorder.dto.isEmpty
import com.omar.retromp3recorder.storage.repo.global.LogsRepo
import com.omar.retromp3recorder.utils.AmplitudaDealer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class WaveformScanUpdaterUC @Inject constructor(
    private val amplitudaDealer: AmplitudaDealer,
    private val dbUpdaterUC: DbUpdaterUC,
    private val fileRepoUpdaterUC: FileRepoUpdaterUC,
    private val waveformScanner: WaveformScanner,
    private val scheduler: Scheduler,
    private val logsRepo: LogsRepo
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
                            fileRepoUpdaterUC.execute(listOf(it)),
                            Completable.fromAction {
                                logsRepo.onNext(
                                    LogEvent.Message(
                                        Stringer(
                                            R.string.waveform_acquired, it.path
                                        )
                                    )
                                )
                            }
                        )
                    )
                }
        }
        return Completable
            .merge(mapIndexed)
            .subscribeOn(scheduler)
    }
}
