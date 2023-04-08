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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.test.assignment.ui.paging.isEmpty
import kotlinx.coroutines.flow.Flow


@Composable
fun Results(
    data: Flow<PagingData<ExistingFileWrapper>>,
    currentFile: ExistingFileWrapper?,
    onClick: (ExistingFileWrapper) -> Unit
) {
    val pagingItems: LazyPagingItems<ExistingFileWrapper> = data.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    val finishedLoading = pagingItems.loadState.append.endOfPaginationReached
    if (pagingItems.isEmpty() && finishedLoading) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = stringResource(id = R.string.no_saved_records_found),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    } else {
        LazyColumn(state = listState) {
            items(items = pagingItems) { file ->
                file?.let {
                    ItemComposable2(
                        itemFile = file, currentFile, onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemComposable2(
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
        Column(Modifier.padding(8.dp)) {
            //top row
            Row() {
                //title
                Text(
                    modifier = Modifier.weight(3f),
                    text = itemFile.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // length
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End,
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
            //bottom row
            Row {
                //created text
                Row(Modifier.weight(3f).padding(top = 4.dp)) {
                    Text(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        text = stringResource(id = R.string.created)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        fontSize = 12.sp,
                        text = itemFile.createTimedStamp.toCreationDate(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                //waveform
                Box(Modifier.weight(2f)) {
                    itemFile.wavetable?.let { wavetable ->
                        WavetableCompose(
                            modifier = Modifier
                                .height(44.dp)
                                .fillMaxWidth(),
                            data = WavetableComposeData(wavetable)
                        )
                    }
                }
            }
        }
    }
}
