package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.dto.Wavetable
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.WavetableRepo
import com.omar.retromp3recorder.storage.repo.WavetableSampleRateRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import timber.log.Timber
import javax.inject.Inject

@Suppress("SameParameterValue")
class RecordWavetableUC @Inject constructor(
    private val recorderMapper: RecordWavetableMapper,
    private val audioStateMapper: AudioStateMapper,
    private val wavetableRepo: WavetableRepo,
    private val wavetableSampleRateRepo: WavetableSampleRateRepo,
    private val currentFileRepo: CurrentFileRepo,
    private val scheduler: Scheduler
) {
    fun execute(): Completable = audioStateMapper.observe()
        .ofType(AudioState.Recording::class.java)
        .flatMapCompletable {
            recorderMapper.observe()
                .takeUntil(audioStateMapper.observe().ofType(AudioState.Idle::class.java))
                .collectInto(mutableListOf<Byte>()) { list, item ->
                    list.add(item)
                }
                .map { it.toByteArray() }
                .flatMapCompletable {
                    Completable.fromAction {
                        val ghost = Wavetable(
                            it,
                            wavetableSampleRateRepo.observe().blockingFirst().value
                        )
                        val currentFilePath = currentFileRepo.observe().blockingFirst().value!!
                        val pair = Pair(
                            currentFilePath,
                            ghost
                        )
                        wavetableRepo.onNext(pair)
                        Timber.d("wavetableRepo.onNext(pair) $pair")
                    }
                }
        }
        .subscribeOn(scheduler)
}

