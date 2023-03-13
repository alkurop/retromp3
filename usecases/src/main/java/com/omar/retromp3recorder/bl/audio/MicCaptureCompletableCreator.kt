package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.files.GetNewFileNameUC
import com.omar.retromp3recorder.bl.files.IncrementFileNameUC
import com.omar.retromp3recorder.bl.system.WakeLockUsecase
import com.omar.retromp3recorder.domain.toFutureFileWrapper
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.Optional
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import javax.inject.Inject

class MicCaptureCompletableCreator @Inject constructor(
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val currentFileRepo: CurrentFileRepo,
    private val incrementFileNameUC: IncrementFileNameUC,
    private val getNewFileNameUC: GetNewFileNameUC,
    private val voiceRecorder: Mp3VoiceRecorder,
    private val wakeLockUsecase: WakeLockUsecase

) {
    fun create(audioSource: Mp3VoiceRecorder.AudioSource): Completable {
        val propsZipper = BiFunction { filepath: String,
                                       prefs: Mp3VoiceRecorder.RecorderPrefs ->
            Mp3VoiceRecorder.RecorderProps(filepath, prefs, audioSource)
        }
        return Single
            .zip(
                getNewFileNameUC.execute(),
                recorderPrefsRepo.takeSingle(),
                propsZipper
            )

            .flatMapCompletable { props: Mp3VoiceRecorder.RecorderProps ->
                Completable.fromAction {
                    currentFileRepo.onNext(Optional(props.filepath.toFutureFileWrapper()))
                    voiceRecorder.recordWithProps(props)
                }
            }
            .andThen(incrementFileNameUC.execute())
            .andThen(wakeLockUsecase.execute())
    }
}
