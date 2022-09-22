package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.files.GenerateDirIfNotExistsUC
import com.omar.retromp3recorder.bl.files.GetNewFileNameUC
import com.omar.retromp3recorder.bl.files.IncrementFileNameUC
import com.omar.retromp3recorder.dto.toFutureFileWrapper
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import javax.inject.Inject

class CaptureCompletableCreator @Inject constructor(
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val currentFileRepo: CurrentFileRepo,
    private val incrementFileNameUC: IncrementFileNameUC,
    private val generateDirIfNotExistsUC: GenerateDirIfNotExistsUC,
    private val getNewFileNameUC: GetNewFileNameUC,
    private val voiceRecorder: Mp3VoiceRecorder
) {
    fun create(audioSource: Mp3VoiceRecorder.AudioSource): Completable {
        val propsZipper = BiFunction { filepath: String,
                                       prefs: Mp3VoiceRecorder.RecorderPrefs ->
            Mp3VoiceRecorder.RecorderProps(filepath, prefs, audioSource)
        }
        return generateDirIfNotExistsUC.execute()
            .andThen(
                Single.zip(
                    getNewFileNameUC.execute(),
                    recorderPrefsRepo.takeOne(),
                    propsZipper
                )
            )
            .flatMapCompletable { props: Mp3VoiceRecorder.RecorderProps ->
                Completable.fromAction {
                    currentFileRepo.onNext(Optional(props.filepath.toFutureFileWrapper()))
                    voiceRecorder.recordWithProps(props)
                }
            }
            .andThen(incrementFileNameUC.execute())
    }
}
