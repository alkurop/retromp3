package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.bl.system.RequestMediaProjectionUCSuspend
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import com.omar.retromp3recorder.utils.domain.repo.first
import javax.inject.Inject

class RecordMediaUC @Inject constructor(
    private val serviceDealer: ServiceDealer,
    private val projectionRepo: MediaProjectionStateRepo,
    private val micCaptureCompletableCreator: AudioCaptureUC,
    private val requestMediaProjectionUC: RequestMediaProjectionUCSuspend,
    private val wavetableUC: RecordWavetableUCSuspend
) {
    suspend fun execute(source: Int) {
        serviceDealer.startMediaProjectionService()
        val mediaProjectionState = projectionRepo.first()
        val projection = mediaProjectionState.mediaProjection.value
        if (projection != null) {
            micCaptureCompletableCreator.execute(
                Mp3VoiceRecorder.AudioSource.Output(
                    projection,
                    source
                )
            )
            wavetableUC.execute()
        } else {
            requestMediaProjectionUC.execute()
        }
    }
}
