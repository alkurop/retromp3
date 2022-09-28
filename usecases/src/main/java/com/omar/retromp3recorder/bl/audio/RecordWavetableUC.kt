package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.system.SaveRecordingWithWavetableUC
import com.omar.retromp3recorder.bl.waveform.RecordWavetableMapper
import com.omar.retromp3recorder.bl.waveform.WavetableSummer
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Suppress("SameParameterValue")
class RecordWavetableUC @Inject constructor(
    private val recorderMapper: RecordWavetableMapper,
    private val audioStateMapper: AudioStateMapper,
    private val saveRecordingWithWavetableUC: SaveRecordingWithWavetableUC,
    private val currentFileRepo: CurrentFileRepo,
    private val scheduler: Scheduler
) {
    fun execute(): Completable =
        recorderMapper
            .observe()
            .takeUntil(audioStateMapper.observe().ofType(AudioState.Idle::class.java))
            .collectInto(WavetableSummer(), WavetableSummer.recordCollectFunction)
            .map { it.toWaveTable() }
            .flatMapCompletable { wavetable ->
                Single.fromCallable {
                    val currentFile = currentFileRepo.observe().blockingFirst().value!!
                    Pair(currentFile.path, wavetable)

                }.flatMapCompletable {
                    saveRecordingWithWavetableUC.execute(it)
                }
            }
            .subscribeOn(scheduler)
}


