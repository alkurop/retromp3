package com.test.assignment.ui.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed

/**
 * Displays [LazyPagingItems] in a [LazyColumn].
 *
 * Depending on the input [data], may display
 * Error, Empty an Loading states.
 *
 * @param data - Result of the Loading request
 * @param contentPadding - Padding for items in the [LazyColumn]
 * @param listState - State for the underlying [LazyColumn]
 * @param emptyView - Shows when no items were loaded
 * @param errorView  - Shows when loading failed with error
 * @param loadingView  - Displays progress indicator
 * @param itemBuilder - Function that returns an item [Composable]
 * @param idProvider - Function that returns a stable unique primitive item id for the [LazyColumn]
 */
@Composable
fun <T : Any> PagingColumnView(
    data: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(all = 0.dp),
    listState: LazyListState = rememberLazyListState(),
    emptyView: @Composable () -> Unit = {},
    errorView: @Composable () -> Unit = {},
    loadingView: @Composable () -> Unit = {},
    itemBuilder: @Composable LazyItemScope.(Int, T) -> Unit,
    idProvider: (T) -> Any,
) {
    val isLoading = data.isLoading()
    val isError = data.isError()
    val isEmpty = data.isEmpty()

    Box {
        LazyPagingColumn(
            data = data,
            modifier = modifier,
            listState = listState,
            contentPadding = contentPadding,
            itemBuilder = itemBuilder,
            idProvider = idProvider
        )
        when {
            isEmpty && isLoading -> loadingView()
            isEmpty && isError -> errorView()
            isEmpty -> emptyView()
        }
    }
}

@Composable
private fun <T : Any> LazyPagingColumn(
    contentPadding: PaddingValues,
    data: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    itemBuilder: @Composable LazyItemScope.(Int, T) -> Unit,
    idProvider: (T) -> Any,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
        state = listState,
        content = {
            itemsIndexed(items = data, key = { _, item -> idProvider(item) }) { index, item ->
                //if item is null, it's possible to add placeholder
                item?.let { itemBuilder(index, item) }
            }
        }
    )
}
