package com.omar.retromp3recorder.app.screens.home.components.joined_progress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.ui.wavetable.compose.*


@Composable
fun JoinedProgressLayout(
    modifier: Modifier = Modifier,
    viewModel: JoinedProgressViewModel = hiltViewModel(),
) {
    val viewState: JoinedProgressContract.State by viewModel.state.collectAsState()

    val onEvent: (JoinedProgressContract.In) -> Unit = remember {
        {
            viewModel.onEvent(it)
        }
    }

    Surface(modifier = modifier) {
        Column() {
            viewState.fileName?.let { fileName ->
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = fileName,
                    style = MaterialTheme.typography.titleSmall
                )
            }


            Box() {
                when (val progress = viewState.seekState) {
                    is JoinedProgressContract.SeekViewState.Recorder -> {
                        BuildPreview(progress.data, modifier = Modifier.fillMaxSize())
                    }
                    is JoinedProgressContract.SeekViewState.Player -> {
                        BuildSeek(
                            progress = progress.data, callback = onEvent
                        )
                    }
                    JoinedProgressContract.SeekViewState.Hidden -> {
                        BuildRecordMessage()
                    }
                    JoinedProgressContract.SeekViewState.Intermediate -> {
                        /* no render */
                    }
                }
            }
        }
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
