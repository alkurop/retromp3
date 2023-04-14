package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TakeLastFileDbItemUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun execute():  ExistingFileWrapper? {
        return withContext(dispatcher) {
            appDatabase.fileEntityDao().takeLast()?.toFileWrapper()
        }
    }
}
