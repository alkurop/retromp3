package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.utils.platform.Optional
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TakeLastFileDbItemUC @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun get(): Single<Optional<ExistingFileWrapper>> = Single.fromCallable {
        Optional(appDatabase.fileEntityDao().takeLast()?.toFileWrapper())
    }
}
