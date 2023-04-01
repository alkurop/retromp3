package com.omar.retromp3recorder.app.screens.home.components.joined_progress

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.ui.wavetable.compose.*


@Composable
fun JoinedProgressLayout(
    modifier: Modifier = Modifier,
    viewModel: JoinedProgressViewModelFlow = hiltViewModel(),
) {
    val viewState: JoinedProgressContract.State by viewModel.state.collectAsState()

    Surface(modifier = modifier) {
        when (val progress = viewState.seekState) {
            is JoinedProgressContract.SeekViewState.Recorder -> {
                BuildPreview(progress.data)
            }
            is JoinedProgressContract.SeekViewState.Player -> {
                BuildSeek(modifier = Modifier, progress = progress.data) {
                    viewModel.onEvent(it)
                }
            }
            JoinedProgressContract.SeekViewState.Hidden -> {
                BuildRecordMessage(modifier)
            }
            JoinedProgressContract.SeekViewState.Intermediate -> {
                /* no render */
            }
        }
        viewState.fileName?.let { fileName -> Text(text = fileName, style = MaterialTheme.typography.titleSmall) }
    }
}

@Composable
private fun BuildPreview(
    data: WavetableComposeData,
    modifier: Modifier = Modifier
) {
    WavetableCompose(
        modifier = modifier,
        data = data
    )
}

@Composable
private fun BuildSeek(
    progress: WaveSeekData,
    modifier: Modifier = Modifier,
    callback: (JoinedProgressContract.In) -> Unit,
) {
    WavetableSeekbarCompose(
        modifier = modifier,
        data = WaveSeekData(
            progress.progress,
            progress.wavetable
        ),
        onEvent = { callback.invoke(it.mapToEvent()) }
    )
}

@Composable
private fun BuildRecordMessage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_file),
        )
    }
}

private fun SeekEvent.mapToEvent(): JoinedProgressContract.In = when (this) {
    is SeekEvent.Seeking -> JoinedProgressContract.In.SeekToPosition(
        this.progress
    )
    is SeekEvent.SeekStarted -> JoinedProgressContract.In.SeekingStarted
    is SeekEvent.SeekFinished -> JoinedProgressContract.In.SeekingFinished
}
