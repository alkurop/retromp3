package com.omar.retromp3recorder.ui.wavetable.compose

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import com.omar.retromp3recorder.domain.PlayerProgress

@Immutable
data class SeekBarData(
    val progress: PlayerProgress,
)

sealed class SeekEvent {
    object SeekStarted : SeekEvent()
    data class Seeking(val progress: Long, val max: Long) : SeekEvent()
    object SeekFinished : SeekEvent()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SeekBarCompose(
    data: SeekBarData,
    onEvent: (SeekEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    var width by remember {
        mutableStateOf(0)
    }
    val k by remember {
        derivedStateOf {
            data.progress.duration / width.toFloat()
        }
    }

    val sendPauseEvent: () -> Unit = remember(onEvent) { { onEvent(SeekEvent.SeekStarted) } }
    val sendUpdateEvent: (Long) -> Unit = remember(onEvent) {
        { update -> onEvent(SeekEvent.Seeking(update, data.progress.duration)) }
    }
    val sendResumeEvent: () -> Unit = remember(onEvent) { { onEvent(SeekEvent.SeekFinished) } }
    val pointerModifier = modifier
        .pointerInteropFilter { event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    sendPauseEvent()
                    val currentProgress = event.toUpdateEvent(data.progress.duration, k)
                    sendUpdateEvent(currentProgress)
                }
                MotionEvent.ACTION_MOVE -> {
                    val currentProgress = event.toUpdateEvent(data.progress.duration, k)
                    sendUpdateEvent(currentProgress)
                }
                MotionEvent.ACTION_UP -> {
                    sendResumeEvent()
                }
            }
            true
        }
        .onSizeChanged {

            width = it.width
        }

    ProgressBar(
        modifier = pointerModifier,
        data = data.progress.toProgressBarData(),
    )
}

fun MotionEvent.toUpdateEvent(duration: Long, k: Float) =
    (k.toLong() * this.x.toInt()).coerceIn(0, duration)

private fun PlayerProgress.toProgressBarData() = ProgressBarData(
    this.progress,
    this.duration
)

@Immutable
private data class ProgressBarData(val progress: Long, val duration: Long)

@Composable
private fun ProgressBar(
    data: ProgressBarData,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val height = size.height

        val max = data.duration
        val current = data.progress

        val currentWidth = current / max.toFloat() * size.width

        drawRect(
            color = Color.Cyan,
            size = Size(currentWidth, height),
            alpha = 0.3f,
            blendMode = BlendMode.Difference
        )
    }
}
