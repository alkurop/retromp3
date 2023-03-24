package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.system.SaveRecordingWithWavetableUCSuspend
import com.omar.retromp3recorder.bl.waveform.RecordWavetableMapperFlow
import com.omar.retromp3recorder.bl.waveform.WavetableSummer
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RecordWavetableUCSuspend @Inject constructor(
    private val recorderMapper: RecordWavetableMapperFlow,
    private val saveRecordingWithWavetableUC: SaveRecordingWithWavetableUCSuspend,
    private val currentFileRepo: CurrentFileRepo,
    private val dispatcher: CoroutineDispatcher
) : CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + dispatcher

    suspend fun execute() {
        job.cancelAndJoin()
        job = Job()
        withContext(coroutineContext) {
            val wavetable = recorderMapper.flow()
                .scan(WavetableSummer(), WavetableSummer.reducer)
                .map { it.toWaveTable() }
                .last()

            val file = requireNotNull(currentFileRepo.first().value) {
                "File should not be null after recording"
            }
            saveRecordingWithWavetableUC.execute(file.path to wavetable)
        }
    }
}


