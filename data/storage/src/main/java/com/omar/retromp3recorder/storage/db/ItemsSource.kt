package com.omar.retromp3recorder.storage.db

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class ItemsLoadRequest(
    val query: String,
    val page: Int
)

class ItemsSource @Inject constructor(
    private val database: AppDatabase,
) : PagingSource<ItemsLoadRequest, ExistingFileWrapper>() {

    override suspend fun load(params: LoadParams<ItemsLoadRequest>): LoadResult<ItemsLoadRequest, ExistingFileWrapper> {
        return try {
            val thisPage = params.key
            val offset = (thisPage?.page ?: 0) * params.loadSize

            val prevPage = thisPage?.let { if (it.page == 0) null else it.copy(page = it.page - 1) }
            val nextPage = thisPage?.let { it.copy(page = it.page + 1) }

            val query = params.key?.query?.let { if (it.isNotBlank()) "%$it%" else null }

            withContext(Dispatchers.IO) {
                val response =
                    if (query == null) {
                        database.fileEntityDao().getAllPaging(params.loadSize, offset)
                    } else {
                        database.fileEntityDao().getAllPagingQuery(query, params.loadSize, offset)
                    }.map { it.toFileWrapper() }

                LoadResult.Page(
                    data = response,
                    prevKey = prevPage,
                    nextKey = if (response.isEmpty()) null else nextPage
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<ItemsLoadRequest, ExistingFileWrapper>): ItemsLoadRequest? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            val prev = anchorPage?.prevKey
            val next = anchorPage?.nextKey

            return prev?.copy(page = prev.page - 1) ?: next?.copy(page = next.page + 1)
        }
    }
}
