package com.omar.retromp3recorder.app.screens.home.components.audio_controls.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.ui.statebutton.InteractiveButton
import com.omar.retromp3recorder.ui.statebutton.InteractiveButtonCombinedState

@Composable
fun PlayButton(
    state: InteractiveButtonCombinedState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = R.drawable.ic_play
    val iconBlink = R.drawable.ic_play_blink
    val contentDescription = R.string.play
    InteractiveButton(
        state = state,
        enabledPainter = painterResource(id = icon),
        blinkPainter = painterResource(id = iconBlink),
        contentDescription = stringResource(id = contentDescription),
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun RecordButton(
    state: InteractiveButtonCombinedState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = R.drawable.ic_record
    val iconBlink = R.drawable.ic_record_blink
    val contentDescription = R.string.record
    InteractiveButton(
        state = state,
        enabledPainter = painterResource(id = icon),
        blinkPainter = painterResource(id = iconBlink),
        contentDescription = stringResource(id = contentDescription),
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun StopButton(
    state: InteractiveButtonCombinedState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = R.drawable.ic_stop
    val contentDescription = R.string.stop
    InteractiveButton(
        state = state,
        enabledPainter = painterResource(id = icon),
        blinkPainter = null,
        contentDescription = stringResource(id = contentDescription),
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun ShareButton(
    state: InteractiveButtonCombinedState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = R.drawable.abc_ic_menu_share_mtrl_alpha
    val contentDescription = R.string.share
    InteractiveButton(
        state = state,
        enabledPainter = painterResource(id = icon),
        blinkPainter = null,
        contentDescription = stringResource(id = contentDescription),
        onClick = onClick,
        modifier = modifier
    )
}
