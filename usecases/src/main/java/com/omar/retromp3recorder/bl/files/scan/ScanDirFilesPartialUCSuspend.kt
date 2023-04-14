package com.omar.retromp3recorder.bl.files.scan

import com.omar.retromp3recorder.bl.database.GetPagingItemsDatabaseUCFlow
import com.omar.retromp3recorder.bl.system.WaveformScanUpdaterUC
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.storage.db.withTransaction
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.toOptional
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class ScanDirFilesPartialUCSuspend @Inject constructor(
    private val appDatabase: AppDatabase,
    private val findFilesUC: FindFilesUCSuspend,
    private val currentFileRepo: CurrentFileRepo,
    private val getPagingItemsDatabaseUC: GetPagingItemsDatabaseUCFlow,
    private val waveformScanUpdaterUC: WaveformScanUpdaterUC,
    private val collector: FileUpdatePayloadCollectorUC,
) {
    suspend fun execute() {
        val foundFiles = findFilesUC.execute()
        val dbItems = getPagingItemsDatabaseUC.flow(FileDbEntityDao.LOAD_SIZE)
            .toList()
            .flatten()

        val payload = collector.execute(dbItems, foundFiles)
        val insertIds = appDatabase.withTransaction {
            appDatabase.fileEntityDao().run {
                delete(payload.deletes)
                update(payload.updates)
                insertBatch(payload.inserts)
            }
        }

        val insertsWithId = payload.inserts.zip(insertIds) { item, id -> item.copy(id = id) }
        val payloadWithId = payload.copy(inserts = insertsWithId)

        val waveScanInput =
            (payloadWithId.inserts + payloadWithId.updates).map { it.toFileWrapper() }
        val waveScanResult = waveformScanUpdaterUC.execute(waveScanInput)

        waveScanResult.firstOrNull()?.let { currentFileRepo.emit(it.toOptional()) }
    }
}
