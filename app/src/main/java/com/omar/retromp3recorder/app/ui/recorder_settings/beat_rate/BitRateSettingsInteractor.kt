package com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate

import com.omar.retromp3recorder.bl.settings.ChangeBitrateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class BitRateSettingsInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val changeBitrateUC: ChangeBitrateUC
) {
    fun processIO(): ObservableTransformer<Mp3VoiceRecorder.BitRate, Mp3VoiceRecorder.BitRate> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<Mp3VoiceRecorder.BitRate> = {
        Observable.merge(
            listOf(
                recorderPrefsRepo.observe().map { it.bitRate }
            )
        )
    }

    private val mapInputToUsecase: (Observable<Mp3VoiceRecorder.BitRate>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(Mp3VoiceRecorder.BitRate::class.java)
                        .flatMapCompletable { changeBitrateUC.execute(it) }
                )
            )
        }
}
