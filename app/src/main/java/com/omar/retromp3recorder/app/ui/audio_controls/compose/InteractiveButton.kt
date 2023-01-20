package com.omar.retromp3recorder.app.ui.audio_controls.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import kotlinx.coroutines.delay

enum class InteractiveButtonState {
    ENABLED,
    DISABLED,
    RUNNING
}

@Composable
fun InteractiveButton(
    state: InteractiveButtonState,
    enabledPainter: Painter,
    blinkPainter: Painter?,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val alpha = if (state == InteractiveButtonState.DISABLED) 0.5f else 1f
    val blinkState = remember { mutableStateOf(false) }
    val image = when (state) {
        InteractiveButtonState.DISABLED -> enabledPainter
        InteractiveButtonState.ENABLED -> enabledPainter
        InteractiveButtonState.RUNNING -> if (blinkState.value && blinkPainter != null) blinkPainter else enabledPainter
    }
    Box(
        modifier
            .aspectRatio(1f)
            .clickable(
                enabled = state != InteractiveButtonState.DISABLED,
                onClick = onClick
            )
            .alpha(alpha = alpha)
    ) {
        Image(
            painter = image,
            contentDescription = contentDescription,
            modifier = modifier.fillMaxSize()
        )
        LaunchedEffect(key1 = state) {
            while (state == InteractiveButtonState.RUNNING && blinkPainter != null) {
                delay(250)
                blinkState.value = !blinkState.value
            }
        }
    }
}