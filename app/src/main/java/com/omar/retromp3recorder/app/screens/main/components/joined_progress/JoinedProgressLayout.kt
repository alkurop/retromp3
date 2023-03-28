package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.utils.toFileName
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.ui.wavetable.BytesWithRange
import com.omar.retromp3recorder.ui.wavetable.WavetablePreview
import com.omar.retromp3recorder.ui.wavetable.WavetableSeekbarPreview

@Composable
fun JoinedProgressLayout(
    modifier: Modifier = Modifier,
    viewModel: JoinedProgressViewModelFlow = hiltViewModel(),
) {
    val viewState: JoinedProgressView.State by viewModel.state.collectAsState()

    Surface(modifier = modifier.height(16.dp)) {
        when (val progress = viewState.joinedProgress) {
            is JoinedProgress.RecorderProgressShown -> {
                BuildPreview(progress)
            }
            is JoinedProgress.PlayerProgressShown -> {
                BuildSeek(progress = progress) {
                    viewModel.onEvent(it)
                }
            }
            JoinedProgress.Hidden -> {
                BuildRecordMessage(modifier)
            }
            JoinedProgress.Intermediate -> {
                /* no render */
            }
        }
        viewState.currentFile?.path?.toFileName()?.let { fileName -> Text(text = fileName) }
    }
}

@Composable
private fun BuildPreview(
    progress: JoinedProgress.RecorderProgressShown,
    modifier: Modifier = Modifier
) {
    AndroidView(modifier = modifier.then(Modifier.fillMaxSize()),
        factory = { context -> WavetablePreview(context) },
        update = { view ->
            view.update(BytesWithRange(progress.wavetable.data, null))
        })
}

@Composable
private fun BuildSeek(
    progress: JoinedProgress.PlayerProgressShown,
    modifier: Modifier = Modifier,
    callback: (JoinedProgressView.In) -> Unit,
) {
    val f: (JoinedProgressView.In) -> Unit = remember {
        {}
    }
    AndroidView(modifier = modifier.then(Modifier.fillMaxWidth()),
        factory = { context ->
            val view = WavetableSeekbarPreview(context)
            view
        },
        update = { view ->
            view.update(progress)
//            compositeDisposable.clear()
//            compositeDisposable += view.observeIsSeeking().subscribe {
//                f.invoke(it.mapToEvent())
//                callback.invoke(it.mapToEvent())
//            }
        })

//    DisposableEffect(key1 = compositeDisposable) {
//        onDispose {
//            compositeDisposable.clear()
//        }
//    }
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

private fun WavetableSeekbarPreview.SeekState.mapToEvent(): JoinedProgressView.In = when (this) {
    is WavetableSeekbarPreview.SeekState.Seeking -> JoinedProgressView.In.SeekToPosition(
        this.progress
    )
    is WavetableSeekbarPreview.SeekState.SeekStarted -> JoinedProgressView.In.SeekingStarted
    is WavetableSeekbarPreview.SeekState.SeekFinished -> JoinedProgressView.In.SeekingFinished
}
