package com.omar.retromp3recorder.bl.audio.actions

import android.media.AudioAttributes.USAGE_GAME
import android.media.AudioAttributes.USAGE_MEDIA
import com.omar.retromp3recorder.bl.audio.MicCaptureSuspendCreator
import com.omar.retromp3recorder.bl.audio.RecordWavetableUC
import com.omar.retromp3recorder.bl.enablers.DeactivatePlayerControlsUC
import com.omar.retromp3recorder.bl.system.RequestMediaProjectionUCSuspend
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class StartRecordUCSuspend @Inject constructor(
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val micCaptureCompletableCreator: MicCaptureSuspendCreator,
    private val deactivatePlayerControlsUC: DeactivatePlayerControlsUC,
    private val projectionRepo: MediaProjectionStateRepo,
    private val requestMediaProjectionUC: RequestMediaProjectionUCSuspend,
    private val serviceDealer: ServiceDealer,
    private val wavetableUC: RecordWavetableUC
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = Job()

    suspend fun execute() {
        withContext(coroutineContext) {
            deactivatePlayerControlsUC.execute().blockingAwait()
        }
        when (recorderPrefsRepo.first().audioSourcePref) {
            Mp3VoiceRecorder.AudioSourcePref.Mic -> executeMic()
            Mp3VoiceRecorder.AudioSourcePref.Media -> executeMedia(USAGE_MEDIA)
            Mp3VoiceRecorder.AudioSourcePref.Games -> executeMedia(USAGE_GAME)
        }
    }

    private suspend fun executeMedia(source: Int) {
        serviceDealer.startMediaProjectionService()
        val mediaProjectionState = projectionRepo.flow().first()
        val projection = mediaProjectionState.mediaProjection.value
        if (projection != null) {
            micCaptureCompletableCreator.execute(
                Mp3VoiceRecorder.AudioSource.Output(
                    projection,
                    source
                )
            )
            //TODO execute wavetable uc
//                .andThen(wavetableUC.execute())
        } else {
            requestMediaProjectionUC.execute()
        }
    }


    private suspend fun executeMic() {
        micCaptureCompletableCreator.execute(Mp3VoiceRecorder.AudioSource.Mic)

        //TODO execute wavetable uc
//                .andThen(wavetableUC.execute())
    }
}
