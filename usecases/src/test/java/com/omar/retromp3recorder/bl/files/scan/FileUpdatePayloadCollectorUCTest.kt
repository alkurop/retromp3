package com.omar.retromp3recorder.bl.files.scan

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockWaveformFactory
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.db.toFileWrapper
import org.junit.Assert.assertEquals
import org.junit.Test

class FileUpdatePayloadCollectorUCTest {
    private val tested = FileUpdatePayloadCollectorUC()
    private val deletes = (1..10).map { MockFileFactory.giveExistingFile(it.toLong()) }
    private val inserts = (1..10).map { MockFileFactory.giveExistingFile(it + 100L) }
    private val updates = (1..10).map {
        MockFileFactory.giveExistingFile(it + 1000L)
            .copy(wavetable = MockWaveformFactory.giveWaveform())
    }
    private val unchanged = (1..10).map {
        MockFileFactory.giveExistingFile(it + 10_000L)
            .copy(wavetable = MockWaveformFactory.giveWaveform())
    }

    @Test
    fun `inserts deletes footprint are identified`() {
        val existingFiles = updates + unchanged + inserts
        val dbFiles = (deletes + unchanged + updates).map { it.toDatabaseEntity() }

        val result = tested.execute(dbFiles, existingFiles)
        assertEquals(deletes, result.deletes.map { it.toFileWrapper() })
        assertEquals(inserts, result.inserts.map { it.toFileWrapper() })
        assertEquals(dbFiles.map { it.filepath }, result.footprintPathList)
    }


    @Test
    fun `waveform updates found`() {
        val existingFiles = updates + unchanged + inserts
        val dbFiles =
            (deletes + unchanged + updates.map { it.copy(wavetable = null) }).map { it.toDatabaseEntity() }

        val result = tested.execute(dbFiles, existingFiles)

        assertEquals(updates.map { it.id }, result.updates.map { it.id })
    }

    @Test
    fun `length updates found`() {
        val existingFiles = updates + unchanged + inserts
        val dbFiles =
            (deletes + unchanged + updates.map { it.copy(length = 1) }).map { it.toDatabaseEntity() }

        val result = tested.execute(dbFiles, existingFiles)

        assertEquals(updates.map { it.id }, result.updates.map { it.id })
    }

    @Test
    fun `created timestamp updates found`() {
        val existingFiles = updates + unchanged + inserts
        val dbFiles =
            (deletes + unchanged + updates.map { it.copy(createTimedStamp = 1) }).map { it.toDatabaseEntity() }

        val result = tested.execute(dbFiles, existingFiles)

        assertEquals(updates.map { it.id }, result.updates.map { it.id })
    }

    @Test
    fun `modified timestamp updates found`() {
        val existingFiles = updates + unchanged + inserts
        val dbFiles =
            (deletes + unchanged + updates.map { it.copy(modifiedTimestamp = 1) }).map { it.toDatabaseEntity() }

        val result = tested.execute(dbFiles, existingFiles)

        assertEquals(updates.map { it.id }, result.updates.map { it.id })
    }

    @Test
    fun `on update length, created, modified and name are changed`() {
        val newTimestamp = 30L

        val existingFiles = updates.map {
            it.copy(
                modifiedTimestamp = newTimestamp,
                createTimedStamp = newTimestamp,
                length = newTimestamp
            )
        } + unchanged + inserts
        val dbFiles =
            (deletes + unchanged + updates).map { it.toDatabaseEntity() }

        val result = tested.execute(dbFiles, existingFiles)

        assertEquals(updates.map {
            it.toDatabaseEntity()
                .copy(lastModified = newTimestamp, created = newTimestamp, length = newTimestamp)
        }, result.updates)
    }
}
