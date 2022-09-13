package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.waveform.RecordWavetableMapper
import com.omar.retromp3recorder.bl.waveform.WavetableSummer
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.WavetableRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import timber.log.Timber
import javax.inject.Inject

@Suppress("SameParameterValue")
class RecordWavetableUC @Inject constructor(
    private val recorderMapper: RecordWavetableMapper,
    private val audioStateMapper: AudioStateMapper,
    private val wavetableRepo: WavetableRepo,
    private val currentFileRepo: CurrentFileRepo,
    private val scheduler: Scheduler
) {
    fun execute(): Completable = audioStateMapper.observe()
        .ofType(AudioState.Recording::class.java)
        .flatMapCompletable {
            recorderMapper.observe()
                .takeUntil(audioStateMapper.observe().ofType(AudioState.Idle::class.java))
                .collectInto(WavetableSummer(), WavetableSummer.recordCollectFunction)
                .map { it.toWaveTable() }
                .flatMapCompletable { wavetable ->
                    Completable.fromAction {
                        val currentFile = currentFileRepo.observe().blockingFirst().value!!

                        val pair = Pair(currentFile.path, wavetable)
                        wavetableRepo.onNext(pair)
                        Timber.d("wavetableRepo.onNext(pair) $pair")
                    }
                }
        }
        .subscribeOn(scheduler)
}


