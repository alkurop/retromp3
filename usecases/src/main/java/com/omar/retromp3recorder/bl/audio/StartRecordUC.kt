package com.omar.retromp3recorder.bl.audio

import android.Manifest
import android.media.AudioAttributes.USAGE_GAME
import android.media.AudioAttributes.USAGE_MEDIA
import com.omar.retromp3recorder.bl.system.CheckPermissionsUC
import com.omar.retromp3recorder.bl.system.RequestMediaProjectionUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.MediaProjectionStateRepo
import com.omar.retromp3recorder.storage.repo.PermissionsRequestBus
import com.omar.retromp3recorder.storage.repo.PermissionsRequestBus.ShouldRequestPermissions
import com.omar.retromp3recorder.storage.repo.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.ServiceDealer
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class StartRecordUC @Inject constructor(
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val checkPermissionsUC: CheckPermissionsUC,
    private val captureCompletableCreator: CaptureCompletableCreator,
    private val permissionsRequestBus: PermissionsRequestBus,
    private val projectionRepo: MediaProjectionStateRepo,
    private val requestMediaProjectionUC: RequestMediaProjectionUC,
    private val serviceDealer: ServiceDealer,
) {
    fun execute(): Completable {
        fun executeMedia(source: Int) = Completable
            .fromAction { serviceDealer.startMediaProjectionService() }
            .andThen(projectionRepo.observe().takeOne())
            .flatMapCompletable {
                val projection = it.mediaProjection.value
                if (projection != null) {
                    captureCompletableCreator.create(
                        Mp3VoiceRecorder.AudioSource.Output(
                            projection,
                            source
                        )
                    )
                } else {
                    requestMediaProjectionUC.execute()
                }
            }

        fun executeMic() = captureCompletableCreator.create(Mp3VoiceRecorder.AudioSource.Mic)

        val abort = Completable.complete()
        return checkPermissionsUC
            .execute(voiceRecordPermissions)
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
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.RECORD_AUDIO
)
