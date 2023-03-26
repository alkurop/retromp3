package com.omar.retromp3recorder.bl.audio.actions

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import com.omar.retromp3recorder.utils.platform.repo.first
import javax.inject.Inject

class StopRecordUCSuspend @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val voiceRecorder: Mp3VoiceRecorder,
    private val serviceDealer: ServiceDealer,
) {
    suspend fun execute() {
        voiceRecorder.stopRecord()
        serviceDealer.stopWakelockService()
        val currentFileWrapper = currentFileRepo.first()
        currentFileRepo.emit(currentFileWrapper)
    }
}
