package com.omar.retromp3recorder.bl.files.scan

import com.omar.retromp3recorder.bl.database.GetPagingItemsDatabaseUCFlow
import com.omar.retromp3recorder.bl.files.FileRepoUpdaterUCSuspend
import com.omar.retromp3recorder.bl.system.WaveformScanUpdaterUCSuspend
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.utils.platform.ScopeJobWrapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScanDirFilesPartialUCSuspend @Inject constructor(
    private val appDatabase: AppDatabase,
    private val findFilesUC: FindFilesUCSuspend,
    private val fileRepoUpdaterUC: FileRepoUpdaterUCSuspend,
    private val getPagingItemsDatabaseUC: GetPagingItemsDatabaseUCFlow,
    private val waveformScanUpdaterUC: WaveformScanUpdaterUCSuspend,
    private val collector: FileUpdatePayloadCollectorUC,
    private val jobWrapper: ScopeJobWrapper
) {
    suspend fun execute(
        shouldCheckEmptyFiles: Boolean = true
    ) {
        jobWrapper.launch {
            val foundFiles = findFilesUC.execute(listOf("mp3"), shouldCheckEmptyFiles)
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

            fileRepoUpdaterUC.execute(updateWithId.inserts.map { it.toFileWrapper() })

            waveformScanUpdaterUC.execute(
                (updateWithId.updates + updateWithId.inserts)
                    .reversed()
                    .map { it.toFileWrapper() })
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

