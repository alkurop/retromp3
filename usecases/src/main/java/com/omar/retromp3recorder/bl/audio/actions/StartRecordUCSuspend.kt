package com.omar.retromp3recorder.bl.audio.actions

import android.media.AudioAttributes.USAGE_GAME
import android.media.AudioAttributes.USAGE_MEDIA
import com.omar.retromp3recorder.bl.audio.record.RecordMediaUC
import com.omar.retromp3recorder.bl.audio.record.RecordMicUC
import com.omar.retromp3recorder.bl.enablers.DeactivatePlayerControlsUCSuspend
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.domain.repo.first
import javax.inject.Inject

class StartRecordUCSuspend @Inject constructor(
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val deactivatePlayerControlsUC: DeactivatePlayerControlsUCSuspend,
    private val recordMediaUC: RecordMediaUC,
    private val recordMicUC: RecordMicUC,
) {

    suspend fun execute() {
        deactivatePlayerControlsUC.execute()
        when (recorderPrefsRepo.first().audioSourcePref) {
            Mp3VoiceRecorder.AudioSourcePref.Mic -> recordMicUC.execute()
            Mp3VoiceRecorder.AudioSourcePref.Media -> recordMediaUC.execute(USAGE_MEDIA)
            Mp3VoiceRecorder.AudioSourcePref.Games -> recordMediaUC.execute(USAGE_GAME)
        }
    }
}
