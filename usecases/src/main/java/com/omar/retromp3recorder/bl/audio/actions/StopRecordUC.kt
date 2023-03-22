package com.omar.retromp3recorder.bl.audio.actions

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.Constants.MAIN_THREAD
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import com.omar.retromp3recorder.utils.domain.takeObservableOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

class StopRecordUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val scheduler: Scheduler,
    private val voiceRecorder: Mp3VoiceRecorder,
    private val serviceDealer: ServiceDealer,
    @Named(MAIN_THREAD) private val mainThreadScheduler: Scheduler
) {
    fun execute(): Completable = Completable
        .fromAction { voiceRecorder.stopRecord() }
        .subscribeOn(scheduler)
        .andThen(Completable.fromAction { serviceDealer.stopWakelockService() })
        .subscribeOn(mainThreadScheduler)
        .andThen(
            currentFileRepo.observe().subscribeOn(scheduler).takeObservableOne()
                .flatMapCompletable { currentFileWrapper ->
                    //silly way to update current file preview and show wavetable of recently
                    //recorded file
                    Completable.fromAction {
                        currentFileRepo.tryNext(currentFileWrapper)
                    }
                })
        .subscribeOn(scheduler)
}
