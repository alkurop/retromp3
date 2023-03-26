package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TakeLastFileDbItemUCSuspend @Inject constructor(
    private val appDatabase: AppDatabase
) {
    private var coroutineContext: Job = Job()

    suspend fun execute():  ExistingFileWrapper? {
        coroutineContext.cancelAndJoin()
        coroutineContext = Job()
        return withContext(coroutineContext) {
            appDatabase.fileEntityDao().takeLast()?.toFileWrapper()
        }
    }
}
