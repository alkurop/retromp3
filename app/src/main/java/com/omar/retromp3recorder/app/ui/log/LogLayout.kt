package com.omar.retromp3recorder.app.ui.log.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.ui.log.LogView
import com.omar.retromp3recorder.app.ui.log.LogViewModel

@Composable
fun LogLayout(
    modifier: Modifier = Modifier, viewModel: LogViewModel = viewModel()
) {
    val state by viewModel.state.subscribeAsState(initial = LogView.State())

    val listState = rememberLazyListState()
    val messages = state.messages

    if(messages.isNotEmpty()) {
        LazyColumn(modifier, state = listState) {
            items(messages.size, { it }) { index ->
                when (val item = messages[index]) {
                    is LogView.Output.ErrorLogOutput -> {
                        ErrorLog(data = item)
                    }
                    is LogView.Output.MessageLogOutput -> {
                        MessageLog(data = item)
                    }
                }
            }
        }
        LaunchedEffect(messages.size) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
}

@Composable
private fun ErrorLog(data: LogView.Output.ErrorLogOutput) {
    val context = LocalContext.current
    Text(
        text = data.error.bell(context),
        modifier = Modifier,
        maxLines = 1,
        color = Color.Red,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun MessageLog(data: LogView.Output.MessageLogOutput) {
    val context = LocalContext.current

    Text(
        text = data.message.bell(context),
        modifier = Modifier,
        maxLines = 1,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodySmall
    )
}
