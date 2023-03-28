package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import javax.inject.Inject

class RecordMicUC @Inject constructor(
    private val micCaptureCompletableCreator: AudioCaptureUC,
    private val wavetableUC: RecordWavetableUC
) {
    suspend fun execute(){
        micCaptureCompletableCreator.execute(Mp3VoiceRecorder.AudioSource.Mic)
        wavetableUC.execute()
    }
}
