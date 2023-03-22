package com.omar.retromp3recorder.bl.audio.actions

import android.media.AudioAttributes.USAGE_GAME
import android.media.AudioAttributes.USAGE_MEDIA
import com.omar.retromp3recorder.bl.audio.MicCaptureCompletableCreator
import com.omar.retromp3recorder.bl.audio.RecordWavetableUC
import com.omar.retromp3recorder.bl.enablers.DeactivatePlayerControlsUC
import com.omar.retromp3recorder.bl.system.RequestMediaProjectionUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import com.omar.retromp3recorder.utils.domain.takeObservableOne
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class StartRecordUC @Inject constructor(
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val micCaptureCompletableCreator: MicCaptureCompletableCreator,
    private val deactivatePlayerControlsUC: DeactivatePlayerControlsUC,
    private val projectionRepo: MediaProjectionStateRepo,
    private val requestMediaProjectionUC: RequestMediaProjectionUC,
    private val serviceDealer: ServiceDealer,
    private val wavetableUC: RecordWavetableUC
) {
    fun execute(): Completable {
        fun executeMedia(source: Int) =
            Completable
                .fromAction { serviceDealer.startMediaProjectionService() }
                .andThen(projectionRepo.observe().takeObservableOne())
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

        fun executeMic() = micCaptureCompletableCreator.create(Mp3VoiceRecorder.AudioSource.Mic)
            .andThen(wavetableUC.execute())

        return deactivatePlayerControlsUC
            .execute()
            .andThen(
                recorderPrefsRepo.observe()
                    .takeObservableOne()
                    .map { it.audioSourcePref }
                    .flatMapCompletable {
                        @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                        when (it) {
                            Mp3VoiceRecorder.AudioSourcePref.Mic -> executeMic()
                            Mp3VoiceRecorder.AudioSourcePref.Media -> executeMedia(USAGE_MEDIA)
                            Mp3VoiceRecorder.AudioSourcePref.Games -> executeMedia(USAGE_GAME)
                        }
                    })
    }
}
