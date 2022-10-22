package com.omar.retromp3recorder.app.ui.files.selector.jpc.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.utils.toTimeDisplay
import kotlinx.coroutines.flow.Flow


@Composable
fun Results(data: Flow<PagingData<ExistingFileWrapper>>, query: () -> String) {
    val pagingItems: LazyPagingItems<ExistingFileWrapper> = data
        .collectAsLazyPagingItems()

    val finishedLoading = pagingItems.loadState.append.endOfPaginationReached
    if (pagingItems.itemCount == 0 && finishedLoading) {
        Text(
            text = stringResource(id = R.string.no_saved_records_found),
            color = MaterialTheme.colors.primary,
        )
    } else {
        LazyColumn {
            items(items = pagingItems) { file ->
                file?.takeIf { it.name.contains(query.invoke(), true) }?.let {
                    ItemComposable(existingFileWrapper = it)
                }
            }
        }
    }
}

@Composable
private fun ItemComposable(existingFileWrapper: ExistingFileWrapper) {
    Card(
        Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
            Text(
                text = existingFileWrapper.name,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = existingFileWrapper.length?.toTimeDisplay()?.time ?: "",
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = existingFileWrapper.createTimedStamp.toTimeDisplay().time ?: "",
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}