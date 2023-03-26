package com.omar.retromp3recorder.bl.database

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPagingItemsDatabaseUCFlow @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun flow(pageSize: Int): Flow<List<FileDbEntity>> {
        return flow {
            val fileEntityDao = appDatabase.fileEntityDao()
            var offsetIncrement = 0
            do {
                val res = fileEntityDao.getAllPaging(pageSize, offsetIncrement)
                if (res.isNotEmpty()) {
                    emit(res)
                }
                offsetIncrement += pageSize
            } while (res.isNotEmpty())
        }
    }
}
