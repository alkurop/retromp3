package com.test.assignment.ui.paging

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

/**
 * Shows if either source or mediator are in loading stage.
 * This could mean that data is available, although loading next page is in progress.
 */
fun LazyPagingItems<*>.isEmpty(): Boolean {
    return this.itemCount == 0
}

/**
 * Check if one of network or database requests failed
 */
fun LazyPagingItems<*>.getError(): Throwable? {
    return allStates()
        .map { it as? LoadState.Error }
        .firstOrNull()?.error
}

/**
 * Shows if either source or mediator in any of loading stages failed.
 * This could mean that data is available, although loading next page failed.
 */
internal fun LazyPagingItems<*>.isError(): Boolean {
    return getError() != null
}

/**
 * Shows loading state for both network and database / mediator
 */
internal fun LazyPagingItems<*>.isLoading(): Boolean {
    val listOf = allStates()
    return LoadState.Loading in listOf
}

private fun LazyPagingItems<*>.allStates() = listOfNotNull(
    this.loadState.source.refresh,
    this.loadState.source.prepend,
    this.loadState.source.append,
    this.loadState.mediator?.append,
    this.loadState.mediator?.prepend,
    this.loadState.mediator?.refresh,
)
