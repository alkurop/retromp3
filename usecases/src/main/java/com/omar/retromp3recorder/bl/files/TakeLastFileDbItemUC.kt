package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TakeLastFileDbItemUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute():  ExistingFileWrapper? {
        scopeJobWrapper.cancelAndJoin()
        return withContext(scopeJobWrapper.coroutineContext) {
            appDatabase.fileEntityDao().takeLast()?.toFileWrapper()
        }
    }
}
