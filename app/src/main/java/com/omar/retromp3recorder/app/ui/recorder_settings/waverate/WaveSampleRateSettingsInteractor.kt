package com.omar.retromp3recorder.app.ui.recorder_settings.waverate

import com.omar.retromp3recorder.bl.settings.ChangeWaveSampleRateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.WavetableSampleRateRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class WaveSampleRateSettingsInteractor @Inject constructor(
    private val changeSampleRateUC: ChangeWaveSampleRateUC,
    private val scheduler: Scheduler,
    private val sampleRateRepo: WavetableSampleRateRepo
) {
    fun processIO(): ObservableTransformer<Mp3VoiceRecorder.WaveTableSampleRate, Mp3VoiceRecorder.WaveTableSampleRate> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<Mp3VoiceRecorder.WaveTableSampleRate> = {
        Observable.merge(
            listOf(
                sampleRateRepo.observe()
            )
        )
    }

    private val mapInputToUsecase: (Observable<Mp3VoiceRecorder.WaveTableSampleRate>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(Mp3VoiceRecorder.WaveTableSampleRate::class.java)
                        .flatMapCompletable { changeSampleRateUC.execute(it) }
                )
            )
        }
}
