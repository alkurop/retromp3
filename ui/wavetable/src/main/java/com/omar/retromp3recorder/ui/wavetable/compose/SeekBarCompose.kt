package com.omar.retromp3recorder.ui.wavetable.compose

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize
import com.omar.retromp3recorder.domain.PlayerProgress

@Immutable
data class SeekBarData(
    val progress: PlayerProgress,
)

sealed class SeekEvent {
    object SeekStarted : SeekEvent()
    data class SeekingFinished(val progress: Long, val max: Long) : SeekEvent()
}

@Stable
class SeekBarState(initialData: SeekBarData) {
    var data by mutableStateOf(initialData)
    var isSeekeing by mutableStateOf(false)
    var update by mutableStateOf(0L)
    var width by mutableStateOf(0)
    val k by derivedStateOf { data.progress.duration.div(width.toFloat()) }

    fun onUpdate(update: Long) {
        this.update = update
        data = data.copy(progress = data.progress.copy(progress = update))
    }
}

@Composable
fun rememberSeekBarState(initialData: SeekBarData): SeekBarState {
    return remember {
        SeekBarState(initialData)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SeekBarCompose(
    state: SeekBarState,
    onEvent: (SeekEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sendPauseEvent: () -> Unit = remember(onEvent) {
        {
            onEvent(SeekEvent.SeekStarted)
            state.isSeekeing = true
        }
    }
    val sendUpdateEvent: (Long) -> Unit = remember(onEvent) {
        { update ->
            state.onUpdate(update)
        }
    }
    val sendResumeEvent: () -> Unit = remember(onEvent) {
        {
            onEvent(SeekEvent.SeekingFinished(state.update, state.data.progress.duration))
            state.isSeekeing = false
        }
    }

    val requestDisallowInterceptTouchEvent = remember { RequestDisallowInterceptTouchEvent() }

    val pointerModifier = modifier
        .pointerInteropFilter(
            requestDisallowInterceptTouchEvent
        ) { event: MotionEvent ->
            requestDisallowInterceptTouchEvent(true)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    sendPauseEvent()
                    val currentProgress = event.toUpdateEvent(state.data.progress, state.k)
                    sendUpdateEvent(currentProgress)
                }


                MotionEvent.ACTION_MOVE -> {
                    val currentProgress = event.toUpdateEvent(state.data.progress, state.k)
                    sendUpdateEvent(currentProgress)
                }
                MotionEvent.ACTION_UP -> sendResumeEvent()
            }
            true
        }
        .onSizeChanged { state.width = it.width }
    ProgressBar(modifier = pointerModifier, data = state.data)
}

fun MotionEvent.toUpdateEvent(progress: PlayerProgress, k: Float): Long {
    val range = progress.range
    val fromRange = progress.duration.div(range.max).times(range.from)
    val toRange = progress.duration.div(range.max).times(range.to)
    val shouldLimitToRange = range.isEnabled
    val min = if (shouldLimitToRange) fromRange else 0
    val max = if (shouldLimitToRange) toRange else progress.duration
    return if (progress.duration > Int.MAX_VALUE) {
        k.div(1000).times(this.x).toLong().times(100).coerceIn(min, max)
    } else {
        k.times(this.x).toLong().coerceIn(min, max)
    }
}

@Composable
private fun ProgressBar(
    data: SeekBarData,
    modifier: Modifier = Modifier
) {
    var size by remember { mutableStateOf(Size(0f, 0f)) }

    Canvas(modifier = modifier.onSizeChanged { size = it.toSize(); }) {
        val height = size.height

        val max = data.progress.duration
        val current = data.progress.progress
        val range = data.progress.range

        val rangeXStart = size.width.div(range.max.toFloat()).times(range.from)
        val rangeXEnd = size.width.div(range.max.toFloat()).times(range.to)

        if (range.isEnabled) {
            val currentWidth = current.div(max.toFloat()) * size.width
            drawRect(
                topLeft = Offset(rangeXStart, 0f),
                size = Size(currentWidth - rangeXStart, height),
                color = Color.Cyan,
                alpha = 0.3f,
                blendMode = BlendMode.Difference
            )
            drawRect(
                size = Size(rangeXStart, height),
                color = Color.Black,
                alpha = 0.5f,
                blendMode = BlendMode.Darken
            )
            drawRect(
                topLeft = Offset(rangeXEnd, 0f),
                size = Size(size.width - rangeXEnd, height),
                color = Color.Black,
                alpha = 0.5f,
                blendMode = BlendMode.Darken
            )
        } else {
            val currentWidth = current.div(max.toFloat()) * size.width
            drawRect(
                color = Color.Cyan,
                size = Size(currentWidth, height),
                alpha = 0.3f,
                blendMode = BlendMode.Difference
            )
        }

        val width = Stroke.DefaultMiter
        if (range.from > 0) {
            drawLine(
                color = Color.White,
                start = Offset(rangeXStart - width, 0f),
                end = Offset(rangeXStart - width, size.height),
                strokeWidth = width
            )
        }
        if (range.to < range.max) {
            drawLine(
                color = Color.White,
                start = Offset(rangeXEnd, 0f),
                end = Offset(rangeXEnd, size.height),
                strokeWidth = width
            )
        }
    }
}
