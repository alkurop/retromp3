package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.utils.toFileName
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.ui.wavetable.BytesWithRange
import com.omar.retromp3recorder.ui.wavetable.WavetablePreview
import com.omar.retromp3recorder.ui.wavetable.WavetableSeekbarPreview
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign

@Composable
fun JoinedProgressLayout(viewModel: JoinedProgressViewModel = viewModel(), modifier: Modifier) {
    val viewState: JoinedProgressView.State by viewModel.state.subscribeAsState(initial = JoinedProgressView.State())

    Surface(modifier = modifier) {
        when (val progress = viewState.joinedProgress) {
            is JoinedProgress.RecorderProgressShown -> {
                BuildPreview(progress)
            }
            is JoinedProgress.PlayerProgressShown -> {
                BuildSeek(progress = progress) {
                    viewModel.input.onNext(it)
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
    val compositeDisposable = CompositeDisposable()
    AndroidView(modifier = modifier.then(Modifier.fillMaxSize()),
        factory = { context -> WavetableSeekbarPreview(context) },
        update = { view ->
            view.update(progress)
            compositeDisposable.clear()
            compositeDisposable += view.observeIsSeeking().subscribe {
                callback.invoke(it.mapToEvent())
            }
        })

    DisposableEffect(key1 = compositeDisposable) {
        onDispose {
            compositeDisposable.clear()
        }
    }
}

@Composable
private fun BuildRecordMessage(
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.Center) {
        Text(
            modifier = modifier,
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
