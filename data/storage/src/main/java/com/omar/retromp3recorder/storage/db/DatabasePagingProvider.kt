package com.omar.retromp3recorder.storage.db

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DatabasePagingProvider @Inject constructor(
    private val itemsSource: ItemsSource
) {
    fun createFlow(query: String): Flow<PagingData<ExistingFileWrapper>> {
        return Pager(
            initialKey = ItemsLoadRequest(query, page = 0),
            config = PagingConfig(
                pageSize = FileDbEntityDao.LOAD_SIZE,

                enablePlaceholders = false
            )
        ) {
            itemsSource
        }.flow
    }

}

