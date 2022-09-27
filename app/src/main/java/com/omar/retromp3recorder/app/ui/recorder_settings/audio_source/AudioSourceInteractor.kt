package com.omar.retromp3recorder.app.ui.recorder_settings.audio_source

import com.omar.retromp3recorder.bl.settings.ChangeAudioSourceUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class AudioSourceInteractor @Inject constructor(
    private val repo: RecorderPrefsRepo,
    private val changeAudioSourceUC: ChangeAudioSourceUC,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<Mp3VoiceRecorder.AudioSourcePref, Mp3VoiceRecorder.AudioSourcePref> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<Mp3VoiceRecorder.AudioSourcePref> = {
        Observable.merge(
            listOf(
                repo.observe().map { it.audioSourcePref }
            )
        )
    }

    private val mapInputToUsecase: (Observable<Mp3VoiceRecorder.AudioSourcePref>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(Mp3VoiceRecorder.AudioSourcePref::class.java)
                        .flatMapCompletable { changeAudioSourceUC.execute(it) }
                )
            )
        }
}
