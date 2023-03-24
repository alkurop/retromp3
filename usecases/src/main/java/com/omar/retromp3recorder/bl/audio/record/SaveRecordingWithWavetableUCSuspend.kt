package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.domain.Wavetable
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.FileLister
import com.omar.retromp3recorder.utils.platform.toOptional
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SaveRecordingWithWavetableUCSuspend @Inject constructor(
    private val appDatabase: AppDatabase,
    private val fileLister: FileLister,
    private val saveMp3TagsUC: SaveMp3TagsUCSuspend,
    private val currentFileRepo: CurrentFileRepo,
    private val dispatcher: CoroutineDispatcher
) {
    private var job: Job = Job()
    private val coroutineContext: CoroutineContext
        get() = job + dispatcher

    suspend fun execute(data: Pair<String, Wavetable>) {
        job.cancelAndJoin()
        job = Job()
        withContext(coroutineContext) {
            saveMp3TagsUC.execute(data.first)
            val fileEntityDao = appDatabase.fileEntityDao()
            val newItem = fileLister.discoverFile(data.first)
                .copy(
                    wavetable = data.second,
                    length = fileLister.discoverLength(data.first)
                )
            val id = fileEntityDao.insert(newItem.toDatabaseEntity())
            currentFileRepo.emit(newItem.copy(id).toOptional())
        }
    }
}
