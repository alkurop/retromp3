package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.bl.files.GetNewFileNameUC
import com.omar.retromp3recorder.bl.files.IncrementFileNameUC
import com.omar.retromp3recorder.bl.system.WakeLockUsecase
import com.omar.retromp3recorder.domain.toFutureFileWrapper
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.Optional
import javax.inject.Inject

class AudioCaptureUC @Inject constructor(
    private val recorderPrefsRepo: RecorderPrefsRepo,
    private val currentFileRepo: CurrentFileRepo,
    private val incrementFileNameUC: IncrementFileNameUC,
    private val getNewFileNameUC: GetNewFileNameUC,
    private val voiceRecorder: Mp3VoiceRecorder,
    private val wakeLockUsecase: WakeLockUsecase

) {
    suspend fun execute(audioSource: Mp3VoiceRecorder.AudioSource) {
        val filePath = getNewFileNameUC.execute()
        val prefs = recorderPrefsRepo.first()
        val props = Mp3VoiceRecorder.RecorderProps(filePath, prefs, audioSource)
        currentFileRepo.emit(Optional(props.filepath.toFutureFileWrapper()))
        voiceRecorder.recordWithProps(props)
        incrementFileNameUC.execute()
        wakeLockUsecase.execute()
    }
}
