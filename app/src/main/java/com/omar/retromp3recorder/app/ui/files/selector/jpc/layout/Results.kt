package com.omar.retromp3recorder.app.ui.files.selector.jpc.layout

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.omar.retromp3recorder.app.ui.files.selector.SelectorView
import com.omar.retromp3recorder.app.ui.files.selector.SelectorViewModel
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import kotlinx.coroutines.flow.Flow


@Composable
fun Results(
    viewModel: SelectorViewModel = viewModel(),
    searchQuery: () -> String = { "Hello" }
) {
    val state by viewModel.state.subscribeAsState(initial = SelectorView.State())
    val itemsPaging = state.itemsPaging
    if (itemsPaging == null) {
        Text(
            text = "Empty",
            color = MaterialTheme.colors.primary,
        )
    } else {
        ResultsItems(data = itemsPaging)
    }
}

@Composable
private fun ResultsItems(data: Flow<PagingData<ExistingFileWrapper>>) {
    val pagingItems: LazyPagingItems<ExistingFileWrapper> = data.collectAsLazyPagingItems()
    LazyColumn {
        items(pagingItems) { file ->
            file?.let {
                Text(it.path, color = MaterialTheme.colors.primary)
            }
        }
    }
}

@Composable
private fun ItemComposable() {
}