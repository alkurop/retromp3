package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.bl.database.GetPagingItemsUC
import com.omar.retromp3recorder.bl.system.WaveformScanUpdaterUC
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntity
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.db.toFileWrapper
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ScanDirFilesPartialUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val findFilesUC: FindFilesUC,
    private val fileRepoUpdaterUC: FileRepoUpdaterUC,
    private val getPagingItemsUC: GetPagingItemsUC,
    private val waveformScanUpdaterUC: WaveformScanUpdaterUC
) {
    fun execute(
        shouldCheckEmptyFiles: Boolean = true
    ): Completable {
        return findFilesUC
            .get(listOf("mp3"), shouldCheckEmptyFiles)
            .flatMapCompletable { foundFiles ->
                getPagingItemsUC.observe(10)
                    .flatMapSingle { dbFiles ->
                        Single.fromCallable {
                            val deletes = dbFiles.findDeletes(foundFiles)
                            val inserts = dbFiles.findInserts(foundFiles)
                            val updates = dbFiles.findUpdates(foundFiles)
                            DbUpdateItem(deletes, updates, inserts)
                        }
                    }
                    .collectInto(mutableListOf<DbUpdateItem>()) { list, item -> list.add(item) }
                    .map { it.merge() }
                    .flatMapCompletable { dbUpdateItem ->
                        Completable.merge(
                            listOf(
                                Completable.fromAction {
                                    appDatabase.fileEntityDao().delete(dbUpdateItem.deletes)
                                    appDatabase.fileEntityDao().update(dbUpdateItem.updates)
                                    appDatabase.fileEntityDao().insert(dbUpdateItem.inserts)
                                },
                                fileRepoUpdaterUC.execute(dbUpdateItem.inserts.map { it.toFileWrapper() }),
                                waveformScanUpdaterUC.execute((dbUpdateItem.updates + dbUpdateItem.inserts).map { it.toFileWrapper() })
                            )
                        )
                    }
            }
    }
}

data class DbUpdateItem(
    val deletes: List<FileDbEntity>,
    val updates: List<FileDbEntity>,
    val inserts: List<FileDbEntity>,
)

private fun List<DbUpdateItem>.merge(): DbUpdateItem {
    val updates = this.map { it.updates }.flatten()
    val deletes = this.map { it.deletes }.flatten()
    val otherChanges = updates.plus(deletes)
    val inserts = this.map { it.inserts }.flatten()
        .filter { item -> !otherChanges.map { it.filepath }.contains(item.filepath) }
    return DbUpdateItem(deletes, updates, inserts)
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

fun List<FileDbEntity>.findUpdates(foundFiles: List<ExistingFileWrapper>): List<FileDbEntity> {
    return foundFiles
        .filter { item ->
            this.map { it.filepath }.contains(item.path)

        }.mapNotNull { item ->
            this.firstOrNull { it.filepath == item.path }
                ?.takeIf {
                    it.waveform == null
                            || it.length != item.length
                            || it.created != item.createTimedStamp
                            || item.modifiedTimestamp != it.lastModified
                }
                ?.copy(
                    length = item.length,
                    created = item.createTimedStamp,
                    lastModified = item.modifiedTimestamp
                )
        }
}