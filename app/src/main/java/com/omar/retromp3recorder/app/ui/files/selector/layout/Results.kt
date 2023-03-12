package com.omar.retromp3recorder.app.ui.files.selector.layout

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.ui.wavetable.BytesWithRange
import com.omar.retromp3recorder.ui.wavetable.WavetablePreview
import com.omar.retromp3recorder.utils.domain.toCreationDate
import com.omar.retromp3recorder.utils.domain.toTimeDisplay
import kotlinx.coroutines.flow.Flow


@Composable
fun Results(
    data: Flow<PagingData<ExistingFileWrapper>>,
    currentFilePath: String?,
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
                        existingFileWrapper = existingFileWrapper, currentFilePath, onClick
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
    existingFileWrapper: ExistingFileWrapper,
    currentFilePath: String? = null,
    onClick: (ExistingFileWrapper) -> Unit
) {
    val matches = existingFileWrapper.path == currentFilePath

    Card(
        modifier = Modifier
            .clickable { onClick.invoke(existingFileWrapper) }
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        border = if (matches) BorderStroke(
            5.dp, color = MaterialTheme.colorScheme.secondary
        ) else null) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = existingFileWrapper.name, color = MaterialTheme.colorScheme.onSurface
            )
            existingFileWrapper.wavetable?.let { wavetable ->
                AndroidView(factory = { context ->
                    WavetablePreview(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 100)
                        update(BytesWithRange(wavetable.data, null))
                    }
                })
            }
            val time = existingFileWrapper.length?.toTimeDisplay()?.let {
                "${it.time}${it.millis}"
            } ?: ""
            Row {
                Text(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(id = R.string.created)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    fontSize = 12.sp,
                    text = existingFileWrapper.createTimedStamp.toCreationDate(),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.weight(1f))
                Text(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(id = R.string.length)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, text = time
                )
            }

        }
    }
}
