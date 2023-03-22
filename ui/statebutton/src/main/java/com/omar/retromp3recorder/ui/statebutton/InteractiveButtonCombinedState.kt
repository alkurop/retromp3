package com.omar.retromp3recorder.ui.statebutton

import androidx.compose.runtime.*

@Stable
class InteractiveButtonCombinedState(
    initialState: InteractiveButtonState = InteractiveButtonState.DISABLED
) {
    private val currentState by mutableStateOf<InteractiveButtonState>(initialState)
    val isEnabled by derivedStateOf {
        currentState == InteractiveButtonState.ENABLED
    }
    val isRunning by derivedStateOf {
        currentState == InteractiveButtonState.RUNNING
    }
    val isAlphaFull by derivedStateOf { currentState != InteractiveButtonState.DISABLED }
    var blinkState by mutableStateOf(false)

    val isBlinkPainterEnabled by derivedStateOf {
        when (currentState) {
            InteractiveButtonState.DISABLED,
            InteractiveButtonState.ENABLED -> true
            InteractiveButtonState.RUNNING -> !blinkState
        }
    }
}

@Composable
fun rememberCombinedButtonState(state:InteractiveButtonState): InteractiveButtonCombinedState =
    remember(state) { InteractiveButtonCombinedState(state) }
