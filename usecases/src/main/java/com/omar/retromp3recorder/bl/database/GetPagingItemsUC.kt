package com.omar.retromp3recorder.bl.database

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetPagingItemsUC @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun observe(pageSize: Int): Observable<List<ExistingFileWrapper>> {
        return Observable.create { source ->
            val fileEntityDao = appDatabase.fileEntityDao()
            var offsetIncrement = 0
            do {
                val res = fileEntityDao.getAllPaging(pageSize, offsetIncrement)
                if (res.isNotEmpty()) {
                    source.onNext(res.map { it.toFileWrapper() })
                }
                offsetIncrement += pageSize
            } while (res.isNotEmpty() && source.isDisposed.not())
            source.onComplete()
        }
    }
}
