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
    ): DbUpdatePayload {
        val deletes = dbEntries.findDeletes(existingFiles)
        val inserts = dbEntries.findInserts(existingFiles)
        val updates = dbEntries.findUpdates(existingFiles)

        return DbUpdatePayload(
            deletes = deletes,
            updates = updates,
            inserts = inserts,
            existing = dbEntries.map { it.filepath })
    }

    companion object {

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
    }

}

data class DbUpdatePayload(
    val deletes: List<FileDbEntity>,
    val updates: List<FileDbEntity>,
    val inserts: List<FileDbEntity>,
    val existing: List<String>
)
