package com.omar.retromp3recorder.bl.audio.actions

import android.media.AudioAttributes.USAGE_GAME
import android.media.AudioAttributes.USAGE_MEDIA
import com.omar.retromp3recorder.bl.audio.record.RecordMediaUC
import com.omar.retromp3recorder.bl.audio.record.RecordMicUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.domain.DifferedCoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartRecordUC @Inject constructor(
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val recordMediaUC: RecordMediaUC,
    private val recordMicUC: RecordMicUC,
    private val differedCoroutineScope: DifferedCoroutineScope,
) {

    suspend fun execute() {
        // recording should continue despite of the UI CoroutineScope cancelation,
        // like activity killed, view model scope canceled
        differedCoroutineScope.launch {
            when (recorderPrefsRepo.first().audioSourcePref) {
                Mp3VoiceRecorder.AudioSourcePref.Mic -> recordMicUC.execute()
                Mp3VoiceRecorder.AudioSourcePref.Media -> recordMediaUC.execute(USAGE_MEDIA)
                Mp3VoiceRecorder.AudioSourcePref.Games -> recordMediaUC.execute(USAGE_GAME)
            }
        }
    }
}
