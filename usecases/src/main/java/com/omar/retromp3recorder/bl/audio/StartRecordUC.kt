package com.omar.retromp3recorder.bl.audio

import android.Manifest
import android.media.AudioAttributes.USAGE_GAME
import android.media.AudioAttributes.USAGE_MEDIA
import com.omar.retromp3recorder.bl.enablers.DeactivatePlayerControlsUC
import com.omar.retromp3recorder.bl.system.CheckPermissionsUC
import com.omar.retromp3recorder.bl.system.RequestMediaProjectionUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.storage.repo.global.PermissionsRequestBus
import com.omar.retromp3recorder.storage.repo.global.PermissionsRequestBus.ShouldRequestPermissions
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.ServiceDealer
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class StartRecordUC @Inject constructor(
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val checkPermissionsUC: CheckPermissionsUC,
    private val micCaptureCompletableCreator: MicCaptureCompletableCreator,
    private val deactivatePlayerControlsUC: DeactivatePlayerControlsUC,
    private val permissionsRequestBus: PermissionsRequestBus,
    private val projectionRepo: MediaProjectionStateRepo,
    private val requestMediaProjectionUC: RequestMediaProjectionUC,
    private val serviceDealer: ServiceDealer,
    private val wavetableUC: RecordWavetableUC
) {
    fun execute(): Completable {
        fun executeMedia(source: Int) =
            Completable
                .fromAction { serviceDealer.startMediaProjectionService() }
                .andThen(projectionRepo.observe().takeOne())
                .flatMapCompletable {
                    val projection = it.mediaProjection.value
                    if (projection != null) {
                        micCaptureCompletableCreator.create(
                            Mp3VoiceRecorder.AudioSource.Output(
                                projection,
                                source
                            )
                        ).andThen(wavetableUC.execute())
                    } else {
                        requestMediaProjectionUC.execute()
                    }
                }

        fun executeMic() = micCaptureCompletableCreator.create(Mp3VoiceRecorder.AudioSource.Mic).andThen(wavetableUC.execute())

        val abort = Completable.complete()
        return deactivatePlayerControlsUC
            .execute()
            .andThen(checkPermissionsUC.execute(voiceRecordPermissions))
            .andThen(permissionsRequestBus.observe().takeOne())
            .flatMapCompletable { shouldAskPermissions ->
                if (shouldAskPermissions is ShouldRequestPermissions.Granted) {
                    recorderPrefsRepo.observe()
                        .takeOne()
                        .map { it.audioSourcePref }
                        .flatMapCompletable {
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

private val voiceRecordPermissions: Set<String> = setOf(
    Manifest.permission.RECORD_AUDIO
)
