package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.utils.Constants.RECORDING_LENGTH_MULTIPLIER
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.BiFunction
import javax.inject.Inject

class StopRecordAfterTooLongUC @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val recorderMapper: RecordWavetableMapper,
    private val stopRecordUC: StopRecordUC,
    private val scheduler: Scheduler
) {
    fun execute(): Completable = audioStateMapper.observe()
        .ofType(AudioState.Recording::class.java)
        .flatMapCompletable {
            recorderMapper.observe()
                .takeUntil(audioStateMapper.observe().ofType(AudioState.Idle::class.java))
                .scan(0) { count, _ ->
                    count + 1
                }
                .flatMapCompletable {
                    if (it < RECORDING_LENGTH_MULTIPLIER) Completable.never() else
                        stopRecordUC.execute()
                }
        }
        .observeOn(scheduler)
}
