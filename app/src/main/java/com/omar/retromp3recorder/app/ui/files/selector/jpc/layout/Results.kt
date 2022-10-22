package com.omar.retromp3recorder.app.ui.files.selector.jpc.layout

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import kotlinx.coroutines.flow.Flow


@Composable
fun Results(data: Flow<PagingData<ExistingFileWrapper>>, query: () -> String) {
    val pagingItems: LazyPagingItems<ExistingFileWrapper> = data.collectAsLazyPagingItems()
    LazyColumn {
        items(items = pagingItems) { file ->
            file?.takeIf { it.name.contains(query.invoke(), true) }?.let {
                Text(it.path, color = MaterialTheme.colors.primary)
            }
        }
    }
}

@Composable
private fun ItemComposable() {
}