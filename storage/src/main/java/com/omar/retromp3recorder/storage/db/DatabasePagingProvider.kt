package com.omar.retromp3recorder.storage.db

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import javax.inject.Inject


class DatabasePagingProvider @Inject constructor(
    private val database: AppDatabase,
    private val itemsSource: ItemsSource
) {
    fun providePagingFiles(): LiveData<PagedList<FileDbEntity>> {
        return LivePagedListBuilder(
            database.fileEntityDao().getAllPagingData(),
            PagedList
                .Config
                .Builder()
                .setPageSize(FileDbEntityDao.LOAD_SIZE)
                .setPrefetchDistance(1)
                .setEnablePlaceholders(false)
                .build()
        ).build()
    }

    fun provideItemSource(): ItemsSource {
        return itemsSource
    }
}

