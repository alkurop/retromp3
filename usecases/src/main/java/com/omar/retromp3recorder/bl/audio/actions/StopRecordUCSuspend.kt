package com.omar.retromp3recorder.bl.audio.actions

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import javax.inject.Inject

class StopRecordUCSuspend @Inject constructor(
    private val voiceRecorder: Mp3VoiceRecorder,
    private val serviceDealer: ServiceDealer,
) {
    suspend fun execute() {
        voiceRecorder.stopRecord()
        serviceDealer.stopWakelockService()
    }
}
