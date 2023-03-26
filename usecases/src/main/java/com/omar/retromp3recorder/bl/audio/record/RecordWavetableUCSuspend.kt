package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.domain.FutureFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.repo.first
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RecordWavetableUCSuspend @Inject constructor(
    private val saveRecordingWithWavetableUC: SaveRecordingWithWavetableUCSuspend,
    private val collectWavetableUC: CollectWavetableUC,
    private val currentFileRepo: CurrentFileRepo,
    private val dispatcher: CoroutineDispatcher
) {
    private var job: Job = Job()
    private val coroutineContext: CoroutineContext
        get() = job + dispatcher

    suspend fun execute() {
        job.cancelAndJoin()
        job = Job()
        val wavetable = withContext(coroutineContext) {
            collectWavetableUC.execute()
        }
        val file = requireNotNull(currentFileRepo.first().value as? FutureFileWrapper) {
            "File should not be null after recording"
        }

        saveRecordingWithWavetableUC.execute(file.path to wavetable)
    }
}


