package com.omar.retromp3recorder.bl.files.scan

import com.omar.retromp3recorder.bl.database.GetPagingItemsDatabaseUCFlow
import com.omar.retromp3recorder.bl.system.WaveformScanUpdaterUCSuspend
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.domain.toOptional
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScanDirFilesPartialUCSuspend @Inject constructor(
    private val appDatabase: AppDatabase,
    private val findFilesUC: FindFilesUCSuspend,
    private val currentFileRepo: CurrentFileRepo,
    private val getPagingItemsDatabaseUC: GetPagingItemsDatabaseUCFlow,
    private val waveformScanUpdaterUC: WaveformScanUpdaterUCSuspend,
    private val collector: FileUpdatePayloadCollectorUC,
    private val jobWrapper: ScopeJobWrapper
) {
    suspend fun execute() {
        jobWrapper.launch {
            val foundFiles = findFilesUC.execute()
            val mergedList = getPagingItemsDatabaseUC.flow(FileDbEntityDao.LOAD_SIZE)
                .map { dbFiles ->
                    collector.execute(dbFiles, foundFiles)
                }
                .toCollection(mutableListOf())


            val dbUpdateItem = mergedList.merge()
            appDatabase.fileEntityDao().delete(dbUpdateItem.deletes)
            appDatabase.fileEntityDao().update(dbUpdateItem.updates)
            val insertIds =
                appDatabase.fileEntityDao().insertBatch(dbUpdateItem.inserts)
            val insertsWithId =
                dbUpdateItem.inserts.zip(insertIds) { item, id -> item.copy(id = id) }
            val updateWithId = dbUpdateItem.copy(inserts = insertsWithId)

            updateWithId.inserts.map { it.toFileWrapper() }
                .lastOrNull()
                ?.let {
                    currentFileRepo.emit(it.toOptional())
                }

            val result = waveformScanUpdaterUC.execute(
                (updateWithId.updates + updateWithId.inserts)
                    .reversed()
                    .map { it.toFileWrapper() })
            result.lastOrNull()?.let { currentFileRepo.emit(it.toOptional()) }
        }
    }
}

private fun List<DbUpdatePayload>.merge(): DbUpdatePayload {
    val updates = this.map { it.updates }.flatten()
    val deletes = this.map { it.deletes }.flatten()
    val otherChanges = updates.plus(deletes)
    val existing = this.map { it.existing }.flatten()

    val inserts = this.map { it.inserts }.flatten()
        .filter { item -> !otherChanges.map { it.filepath }.contains(item.filepath) }
        .filter { !existing.contains(it.filepath) }


    return DbUpdatePayload(
        deletes = deletes,
        updates = updates,
        inserts = inserts,
        existing = existing
    )
}

