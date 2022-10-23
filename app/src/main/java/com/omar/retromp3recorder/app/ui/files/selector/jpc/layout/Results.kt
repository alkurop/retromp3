package com.omar.retromp3recorder.app.ui.files.selector.jpc.layout

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.ui.wavetable.BytesWithRange
import com.omar.retromp3recorder.ui.wavetable.WavetablePreview
import com.omar.retromp3recorder.utils.toCreationDate
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
@Preview
private fun ItemComposable(
    existingFileWrapper: ExistingFileWrapper = ExistingFileWrapper(
        id = 0,
        path = "my/file_created_by_me.mp3",
        createTimedStamp = 1666485830840,
        wavetable = null,
        length = 100,
        modifiedTimestamp = 1666485830840
    )
) {
    Card(
        Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = existingFileWrapper.name,
                color = MaterialTheme.colors.onSurface
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
                    color = MaterialTheme.colors.onSurface,
                    text = stringResource(id = R.string.created)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    fontSize = 12.sp,
                    text = existingFileWrapper.createTimedStamp.toCreationDate(),
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(Modifier.weight(1f))
                Text(
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onSurface,
                    text = stringResource(id = R.string.length)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onSurface,
                    text = time
                )
            }

        }
    }
}
