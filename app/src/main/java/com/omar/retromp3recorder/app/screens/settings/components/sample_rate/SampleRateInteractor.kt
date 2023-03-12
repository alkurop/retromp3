package com.omar.retromp3recorder.app.screens.settings.components.sample_rate

import com.omar.retromp3recorder.bl.settings.ChangeSampleRateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.domain.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class SampleRateInteractor @Inject constructor(
    private val changeSampleRateUC: ChangeSampleRateUC,
    private val scheduler: Scheduler,
    private val recorderPrefsRepo: RecorderPrefsRepo
) {
    fun processIO(): ObservableTransformer<Mp3VoiceRecorder.SampleRate, Mp3VoiceRecorder.SampleRate> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<Mp3VoiceRecorder.SampleRate> = {
        Observable.merge(
            listOf(
                recorderPrefsRepo.observe().map { it.sampleRate }
            )
        )
    }

    private val mapInputToUsecase: (Observable<Mp3VoiceRecorder.SampleRate>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(Mp3VoiceRecorder.SampleRate::class.java)
                        .flatMapCompletable { changeSampleRateUC.execute(it) }
                )
            )
        }
}
