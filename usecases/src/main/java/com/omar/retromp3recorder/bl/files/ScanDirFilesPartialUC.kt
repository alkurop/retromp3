package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.bl.database.GetPagingItemsDatabaseUC
import com.omar.retromp3recorder.bl.system.WaveformScanUpdaterUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.isEmpty
import com.omar.retromp3recorder.storage.db.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ScanDirFilesPartialUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val findFilesUC: FindFilesUC,
    private val fileRepoUpdaterUC: FileRepoUpdaterUC,
    private val getPagingItemsDatabaseUC: GetPagingItemsDatabaseUC,
    private val waveformScanUpdaterUC: WaveformScanUpdaterUC,
    private val scheduler: Scheduler
) {
    fun execute(
        shouldCheckEmptyFiles: Boolean = true
    ): Completable = findFilesUC
        .get(listOf("mp3"), shouldCheckEmptyFiles)
        .flatMapCompletable { foundFiles ->
            getPagingItemsDatabaseUC.observe(FileDbEntityDao.LOAD_SIZE)
                .flatMapSingle { dbFiles ->
                    Single.fromCallable {
                        val deletes = dbFiles.findDeletes(foundFiles)
                        val inserts = dbFiles.findInserts(foundFiles)
                        val updates = dbFiles.findUpdates(foundFiles)
                        val updatesName =
                            dbFiles.findOnlyNeedToUpdateNameAfterMigrationOfDb(foundFiles)

                        DbUpdateItem(
                            deletes = deletes,
                            updates = updates,
                            inserts = inserts,
                            updateNames = updatesName,
                            existing = dbFiles.map { it.filepath })
                    }
                }
                .collectInto(mutableListOf<DbUpdateItem>()) { list, item -> list.add(item) }
                .map { mergeList ->
                    val merge = mergeList.merge()
                    if (merge.existing.isEmpty()) merge.copy(inserts = foundFiles.map { it.toDatabaseEntity() }) else merge
                }
                .flatMap { dbUpdateItem ->
                    Single
                        .fromCallable {
                            appDatabase.fileEntityDao().delete(dbUpdateItem.deletes)
                            appDatabase.fileEntityDao().update(dbUpdateItem.updates)
                            appDatabase.fileEntityDao().update(dbUpdateItem.updateNames)
                            val insertIds =
                                appDatabase.fileEntityDao().insertBatch(dbUpdateItem.inserts)
                            val insertsWithId =
                                dbUpdateItem.inserts.zip(insertIds) { item, id -> item.copy(id = id) }
                            dbUpdateItem.copy(inserts = insertsWithId)
                        }
                }
                .flatMapCompletable { updateWithId ->
                    Completable.merge(
                        listOf(
                            fileRepoUpdaterUC.execute(updateWithId.inserts.map { it.toFileWrapper() }),
                            waveformScanUpdaterUC
                                .execute(
                                    (updateWithId.updates + updateWithId.inserts)
                                        .reversed()
                                        .map { it.toFileWrapper() })
                                .subscribeOn(scheduler)
                        )
                    )
                }
        }
}


data class DbUpdateItem(
    val deletes: List<FileDbEntity>,
    val updates: List<FileDbEntity>,
    val updateNames: List<FileDbEntity>,
    val inserts: List<FileDbEntity>,
    val existing: List<String>
)

private fun List<DbUpdateItem>.merge(): DbUpdateItem {
    val updates = this.map { it.updates }.flatten()
    val deletes = this.map { it.deletes }.flatten()
    val otherChanges = updates.plus(deletes)
    val existing = this.map { it.existing }.flatten()

    val inserts = this.map { it.inserts }.flatten()
        .filter { item -> !otherChanges.map { it.filepath }.contains(item.filepath) }
        .filter { !existing.contains(it.filepath) }

    val updateNames = this.map { it.updateNames }.flatten()
        .filter { it !in updates && it !in deletes && it !in inserts }

    return DbUpdateItem(
        deletes = deletes,
        updates = updates,
        updateNames = updateNames,
        inserts = inserts,
        existing = existing
    )
}

fun List<FileDbEntity>.findDeletes(foundFiles: List<ExistingFileWrapper>): List<FileDbEntity> {
    return this.filter { item ->
        !foundFiles.map { it.path }.contains(item.filepath)
    }
}

fun List<FileDbEntity>.findInserts(foundFiles: List<ExistingFileWrapper>): List<FileDbEntity> {
    return foundFiles.filter { item ->
        !this.map { it.filepath }.contains(item.path)
    }.map { it.toDatabaseEntity() }
}

fun List<FileDbEntity>.findOnlyNeedToUpdateNameAfterMigrationOfDb(foundFiles: List<ExistingFileWrapper>): List<FileDbEntity> {
    return foundFiles
        .filter { item ->
            this.map { it.filepath }.contains(item.path)

        }.mapNotNull { item ->
            this.firstOrNull { it.filepath == item.path }
                ?.takeIf {
                    it.name == null
                }
                ?.copy(
                    name = item.name
                )
        }
}

fun List<FileDbEntity>.findUpdates(foundFiles: List<ExistingFileWrapper>): List<FileDbEntity> {
    return foundFiles
        .filter { item ->
            this.map { it.filepath }.contains(item.path)

        }.mapNotNull { item ->
            this.firstOrNull { it.filepath == item.path }
                ?.takeIf {
                    it.waveform?.waveform.isEmpty()
                            || it.length != item.length
                            || it.created != item.createTimedStamp
                            || item.modifiedTimestamp != it.lastModified
                }
                ?.copy(
                    length = item.length,
                    created = item.createTimedStamp,
                    lastModified = item.modifiedTimestamp,
                    name = item.name
                )
        }
}
