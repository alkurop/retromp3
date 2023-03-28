package com.omar.retromp3recorder.app.utils

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.omar.retromp3recorder.utils.domain.LoadingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Utility method to cache a [PagingData] in [CoroutineScope]
 *
 * @param scope - Scope where the [PagingData] will be cached
 * @param T - type of data
 */
fun <T : Any> LoadingState<Flow<PagingData<T>>>.cacheInViewModel(scope: CoroutineScope): LoadingState<Flow<PagingData<T>>> {
    return when (this) {
        is LoadingState.Success -> {
            LoadingState.Success(this.data.run {
                // cache paging items in view model scope
                this.cachedIn(scope)
            })
        }
        else -> this
    }
}

/**
 * Utility method to map item in a [PagingData]
 *
 * @param I - input
 * @param R - result
 */
fun <I : Any, R : Any> LoadingState<Flow<PagingData<I>>>.map(mapper: (I) -> R): LoadingState<Flow<PagingData<R>>> {
    return when (this) {
        is LoadingState.Success -> {
            val mapped = this.data.map {
                it.map { input ->
                    mapper(input)
                }
            }
            LoadingState.Success(mapped)
        }
        is LoadingState.Loading -> LoadingState.Loading()
        is LoadingState.Error -> LoadingState.Error(this.cause)
    }
}
