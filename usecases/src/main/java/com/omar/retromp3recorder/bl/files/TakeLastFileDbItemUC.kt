package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TakeLastFileDbItemUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val audioCoroutineContext: AudioCoroutineContext
) {
    suspend fun execute():  ExistingFileWrapper? {
        audioCoroutineContext.cancelAndJoin()
        return withContext(audioCoroutineContext.coroutineContext) {
            appDatabase.fileEntityDao().takeLast()?.toFileWrapper()
        }
    }
}
