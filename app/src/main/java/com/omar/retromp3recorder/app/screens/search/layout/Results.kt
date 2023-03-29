package com.omar.retromp3recorder.app.screens.search.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.ui.wavetable.compose.WavetableCompose
import com.omar.retromp3recorder.ui.wavetable.compose.WavetableComposeData
import com.omar.retromp3recorder.utils.platform.toCreationDate
import com.omar.retromp3recorder.utils.platform.toTimeDisplay
import kotlinx.coroutines.flow.Flow


@Composable
fun Results(
    data: Flow<PagingData<ExistingFileWrapper>>,
    currentFile: ExistingFileWrapper?,
    query: String,
    onClick: (ExistingFileWrapper) -> Unit
) {

    val pagingItems: LazyPagingItems<ExistingFileWrapper> = data.collectAsLazyPagingItems()

    val listState = rememberLazyListState()

    val finishedLoading = pagingItems.loadState.append.endOfPaginationReached
    if (pagingItems.itemCount == 0 && finishedLoading) {
        Text(
            text = stringResource(id = R.string.no_saved_records_found),
            color = MaterialTheme.colorScheme.primary,
        )
    } else {
        LazyColumn(state = listState) {
            items(items = pagingItems) { file ->
                file?.takeIf { it.filter(query) }?.let { existingFileWrapper ->
                    ItemComposable(
                        itemFile = existingFileWrapper, currentFile, onClick
                    )
                }
            }
        }
    }
}

private fun ExistingFileWrapper.filter(query: String): Boolean {
    return this.name.contains(query, true)
}

@Composable
private fun ItemComposable(
    itemFile: ExistingFileWrapper,
    selectedFile: ExistingFileWrapper? = null,
    onClick: (ExistingFileWrapper) -> Unit
) {
    val matches = itemFile.id == selectedFile?.id
    val time = itemFile.length?.toTimeDisplay()?.let {
        "${it.time}${it.millis}"
    } ?: ""

    val border = if (matches) BorderStroke(
        1.dp, color = MaterialTheme.colorScheme.secondary
    ) else null
    Card(modifier = Modifier
        .clickable { onClick.invoke(itemFile) }
        .padding(top = 8.dp)
        .padding(horizontal = 8.dp)
        .fillMaxWidth()
        .height(58.dp),
        border = border
    ) {
        Row(Modifier.padding(8.dp)) {
            Column(
                Modifier
                    .weight(2f)
                    .wrapContentHeight()
            ) {

                Text(
                    text = itemFile.name, color = MaterialTheme.colorScheme.onSurface
                )
                Row {
                    Text(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        text = stringResource(id = R.string.created)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        fontSize = 12.sp,
                        text = itemFile.createTimedStamp.toCreationDate(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            itemFile.wavetable?.let { wavetable ->
                WavetableCompose(
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                        .align(CenterVertically),
                    data = WavetableComposeData(wavetable)
                )
            }
            Column(
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(start = 16.dp)
                    .weight(0.7f)
            ) {
                Text(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(id = R.string.length)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, text = time
                )
            }
        }
    }
}
