package com.omar.retromp3recorder.app.ui.files.preview.buttonstate

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RenameFileButtonStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val currentFileRepo: CurrentFileRepo
) {
    fun observe(): Observable<Boolean> {
        return Observable.combineLatest(
            currentFileRepo.observe(),
            audioStateMapper.observe()
        ) { currentFile, audioState ->
            when (audioState) {
                is AudioState.Idle -> currentFile.value != null
                else -> false
            }
        }
    }
}
