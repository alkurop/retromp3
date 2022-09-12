package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.system.SaveRecordingWithWavetableUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class StopRecordUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val saveRecordingWithWavetableUC: SaveRecordingWithWavetableUC,
    private val scheduler: Scheduler,
    private val voiceRecorder: Mp3VoiceRecorder
) {
    fun execute(): Completable = Completable
        .fromAction {
            voiceRecorder.stopRecord()
        }
        .andThen(saveRecordingWithWavetableUC.execute())
        .andThen(currentFileRepo.observe().takeOne().flatMapCompletable { currentFileWrapper ->
            //silly way to update current file preview and show wavetable of recently
            //recorded file
            Completable.fromAction {
                currentFileRepo.onNext(currentFileWrapper)
            }
        })
        .subscribeOn(scheduler)
}