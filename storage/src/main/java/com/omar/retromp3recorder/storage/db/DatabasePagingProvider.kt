package com.omar.retromp3recorder.storage.db

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omar.retromp3recorder.dto.ExistingFileWrapper
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

class ItemsSource @Inject constructor(
    private val database: AppDatabase,
) : PagingSource<Int, ExistingFileWrapper>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExistingFileWrapper> {
        return try {
            val nextPage = params.key ?: 0
            val offset = nextPage * params.loadSize
            val response =
                database.fileEntityDao().getAllPaging(params.loadSize, offset).map { it.toFileWrapper() }

            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ExistingFileWrapper>): Int? {
        TODO("Not yet implemented")
    }
}
