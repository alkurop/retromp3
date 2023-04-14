package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.domain.FutureFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import javax.inject.Inject

class RecordWavetableUC @Inject constructor(
    private val saveRecordingWithWavetableUC: SaveRecordingWithWavetableUC,
    private val collectWavetableUC: CollectWavetableUC,
    private val currentFileRepo: CurrentFileRepo,
) {
    suspend fun execute() {
        val wavetable = collectWavetableUC.execute()
        val value = currentFileRepo.first().value
        val file = requireNotNull(value as? FutureFileWrapper) {
            "File should be FutureFileWrapper after recording but was $value"
        }
        saveRecordingWithWavetableUC.execute(file.path to wavetable)
    }
}


