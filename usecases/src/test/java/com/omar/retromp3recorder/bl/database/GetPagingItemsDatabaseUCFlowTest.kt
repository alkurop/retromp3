package com.omar.retromp3recorder.bl.database

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntity
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPagingItemsDatabaseUCFlowTest {
    private lateinit var appDatabase: AppDatabase
    private lateinit var fileEntityDao: FileDbEntityDao
    private lateinit var getFlow: GetPagingItemsDatabaseUCFlow

    @Before
    fun setUp() {
        appDatabase = mockk()
        fileEntityDao = mockk()
        getFlow = GetPagingItemsDatabaseUCFlow(appDatabase)
        every { appDatabase.fileEntityDao() } returns fileEntityDao
    }

    @Test
    fun `flow emits list of entities`() = runTest {
        val pageSize = 10
        val entities = listOf(
            MockFileFactory.giveExistingFile().toDatabaseEntity(),
            MockFileFactory.giveExistingFile().toDatabaseEntity()
        )
        every { fileEntityDao.getAllPaging(pageSize, any()) } answers {
            val offset = arg<Int>(1)
            if (offset == 0) entities else emptyList()
        }
        val flow = getFlow.flow(pageSize)
        val result = flow.toList()
        assertEquals(listOf(entities), result)
    }
}
