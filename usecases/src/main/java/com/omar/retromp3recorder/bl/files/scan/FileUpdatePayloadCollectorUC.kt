package com.omar.retromp3recorder.bl.files.scan

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.isEmpty
import com.omar.retromp3recorder.storage.db.FileDbEntity
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import javax.inject.Inject

class FileUpdatePayloadCollectorUC @Inject constructor() {
    fun execute(
        dbEntries: List<FileDbEntity>,
        existingFiles: List<ExistingFileWrapper>
    ): DbBatchUpdatePayload {
        val deletes = dbEntries.findDeletes(existingFiles)
        val inserts = dbEntries.findInserts(existingFiles)
        val updates = dbEntries.findUpdates(existingFiles)

        return DbBatchUpdatePayload(
            deletes = deletes,
            updates = updates,
            inserts = inserts
        )
    }

    private companion object {
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
            val filterNotNullValues = this
                .associateWith { dbItem -> foundFiles.firstOrNull { dbItem.filepath == it.path } }
                .filterNotNullValues()
            val filter = filterNotNullValues
                .filter { (dbFile, existingFile) ->
                    val noWaveform = dbFile.waveform?.waveform?.isEmpty() ?: true
                    val lengthChanged = dbFile.length != existingFile.length
                    val createdChanged = dbFile.created != existingFile.createTimedStamp
                    val modifiedChanged = dbFile.lastModified != existingFile.modifiedTimestamp

                    noWaveform || lengthChanged || createdChanged || modifiedChanged
                }
            val map = filter
                .map { (dbFile, existingFile) ->
                    dbFile.copy(
                        length = existingFile.length,
                        created = existingFile.createTimedStamp,
                        lastModified = existingFile.modifiedTimestamp,
                    )
                }
            return map
        }

        @Suppress("UNCHECKED_CAST")
        fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> =
            filterValues { it != null } as Map<K, V>
    }
}

data class DbBatchUpdatePayload(
    val deletes: List<FileDbEntity>,
    val updates: List<FileDbEntity>,
    val inserts: List<FileDbEntity>,
)

