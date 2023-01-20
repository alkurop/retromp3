package com.omar.retromp3recorder.app.ui.audio_controls.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsViewModel

@Composable
fun AudioControlsLayout(viewModel: AudioControlsViewModel = viewModel()) {
    val textWidth = 64.dp
    val textPadding = 8.dp
    val buttonSize = 32.dp
    val spacerWeight = 0.1f

    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.weight(spacerWeight))
        Text(
            text = "00",
            color = Color.White,
            textAlign = TextAlign.End,
            modifier = Modifier
                .width(textWidth)
                .padding(horizontal = textPadding)
        )
        Row(
            Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            PlayButton(
                state = InteractiveButtonState.RUNNING,
                onClick = { },
                Modifier.width(buttonSize)
            )

            RecordButton(
                state = InteractiveButtonState.RUNNING,
                onClick = { },
                Modifier.width(buttonSize)
            )

            StopButton(
                state = InteractiveButtonState.RUNNING,
                onClick = { },
                Modifier.width(buttonSize)
            )

            ShareButton(
                state = InteractiveButtonState.RUNNING,
                onClick = { },
                Modifier.width(buttonSize)
            )
        }
        Text(
            text = "asd",
            color = Color.White,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .width(textWidth)
                .padding(horizontal = textPadding)
        )
        Spacer(modifier = Modifier.weight(spacerWeight))
    }
}

@Composable
fun PlayButton(
    state: InteractiveButtonState,
    onClick: () -> Unit,
    modifier: Modifier
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
        modifier
    )
}

@Composable
fun RecordButton(
    state: InteractiveButtonState,
    onClick: () -> Unit,
    modifier: Modifier
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
        modifier
    )
}

@Composable
fun StopButton(
    state: InteractiveButtonState,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val icon = R.drawable.ic_stop
    val contentDescription = R.string.stop
    InteractiveButton(
        state = state,
        enabledPainter = painterResource(id = icon),
        blinkPainter = null,
        contentDescription = stringResource(id = contentDescription),
        onClick = onClick,
        modifier
    )
}

@Composable
fun ShareButton(
    state: InteractiveButtonState,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val icon = R.drawable.abc_ic_menu_share_mtrl_alpha
    val contentDescription = R.string.share
    InteractiveButton(
        state = state,
        enabledPainter = painterResource(id = icon),
        blinkPainter = null,
        contentDescription = stringResource(id = contentDescription),
        onClick = onClick,
        modifier
    )
}
