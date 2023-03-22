package com.omar.retromp3recorder.ui.statebutton

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    enabledPainter: Painter,
    blinkPainter: Painter?,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: InteractiveButtonCombinedState = remember { InteractiveButtonCombinedState() },
) {
    IconButton(
        onClick = onClick,
        enabled = state.isEnabled,
        modifier = modifier
    ) {
        Icon(
            painter = if (state.isBlinkPainterEnabled) blinkPainter
                ?: enabledPainter else enabledPainter,
            contentDescription = contentDescription,
            modifier = Modifier.alpha(if (state.isAlphaFull) 1f else 0.3f)
        )
        LaunchedEffect(key1 = state) {
            while (state.isRunning && blinkPainter != null) {
                delay(250)
                state.blinkState = !state.blinkState
            }
        }
    }
}
