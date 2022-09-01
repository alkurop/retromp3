package com.omar.retromp3recorder.bl.audio

import android.Manifest
import android.media.AudioAttributes.USAGE_GAME
import android.media.AudioAttributes.USAGE_MEDIA
import com.omar.retromp3recorder.bl.system.CheckPermissionsUC
import com.omar.retromp3recorder.bl.system.RequestMediaProjectionUC
import com.omar.retromp3recorder.bl.files.GenerateDirIfNotExistsUC
import com.omar.retromp3recorder.bl.files.GetNewFileNameUC
import com.omar.retromp3recorder.bl.files.IncrementFileNameUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.*
import com.omar.retromp3recorder.storage.repo.PermissionsRequestBus.ShouldRequestPermissions
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.ServiceDealer
import com.omar.retromp3recorder.utils.takeOneObservable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function3
import javax.inject.Inject

class StartRecordUC @Inject constructor(
    private val audioAudioSourceRepo: AudioSourceRepo,
    private val checkPermissionsUC: CheckPermissionsUC,
    private val captureCompletableCreator: CaptureCompletableCreator,
    private val permissionsRequestBus: PermissionsRequestBus,
    private val projectionRepo: MediaProjectionRepo,
    private val requestMediaProjectionUC: RequestMediaProjectionUC,
    private val serviceDealer: ServiceDealer,
) {
    fun execute(): Completable {
        fun executeMedia(source: Int) = Completable
            .fromAction { serviceDealer.startMediaProjectionService() }
            .andThen(projectionRepo.observe().takeOneObservable())
            .flatMapCompletable {
                val projection = it.value
                if (projection != null) {
                    captureCompletableCreator.create(Mp3VoiceRecorder.AudioSource.Output(projection, source))
                } else {
                    requestMediaProjectionUC.execute()
                }
            }


        fun executeMic() = captureCompletableCreator.create(Mp3VoiceRecorder.AudioSource.Mic)

        val abort = Completable.complete()
        return checkPermissionsUC
            .execute(voiceRecordPermissions)
            .andThen(permissionsRequestBus.observe().takeOneObservable())
            .flatMapCompletable { shouldAskPermissions ->
                if (shouldAskPermissions is ShouldRequestPermissions.Granted) {
                    audioAudioSourceRepo.observe().takeOneObservable().switchMapCompletable {
                        @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                        when (it) {
                            Mp3VoiceRecorder.AudioSourcePref.Mic -> executeMic()
                            Mp3VoiceRecorder.AudioSourcePref.Media -> executeMedia(USAGE_MEDIA)
                            Mp3VoiceRecorder.AudioSourcePref.Games -> executeMedia(USAGE_GAME)
                        }
                    }
                } else {
                    abort
                }
            }
    }
}

class CaptureCompletableCreator @Inject constructor(
    private val bitRateRepo: BitRateRepo,
    private val currentFileRepo: CurrentFileRepo,
    private val incrementFileNameUC: IncrementFileNameUC,
    private val generateDirIfNotExistsUC: GenerateDirIfNotExistsUC,
    private val getNewFileNameUC: GetNewFileNameUC,
    private val sampleRateRepo: SampleRateRepo,
    private val voiceRecorder: Mp3VoiceRecorder
) {
    fun create(audioSource: Mp3VoiceRecorder.AudioSource): Completable {
        val propsZipper = Function3 { filepath: String,
                                      bitRate: Mp3VoiceRecorder.BitRate,
                                      sampleRate: Mp3VoiceRecorder.SampleRate ->
            Mp3VoiceRecorder.RecorderProps(filepath, bitRate, sampleRate, audioSource)
        }
        return generateDirIfNotExistsUC.execute()
            .andThen(
                Observable.zip(
                    getNewFileNameUC.execute().toObservable(),
                    bitRateRepo.observe().takeOneObservable(),
                    sampleRateRepo.observe().takeOneObservable(),
                    propsZipper
                )
            )
            .flatMapCompletable { props: Mp3VoiceRecorder.RecorderProps ->
                Completable.fromAction {
                    currentFileRepo.onNext(Optional(props.filepath))
                    voiceRecorder.recordWithProps(props)
                }
            }
            .andThen(incrementFileNameUC.execute())
    }
}

private val voiceRecordPermissions: Set<String> = setOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.RECORD_AUDIO
)
