package com.omar.retromp3recorder.app.screens.home.components.audio_controls.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.omar.retromp3recorder.app.screens.home.components.audio_controls.AudioControlsView
import com.omar.retromp3recorder.app.screens.home.components.audio_controls.AudioControlsViewModel
import com.omar.retromp3recorder.app.utils.TimeDisplay.toDisplayCompose
import com.omar.retromp3recorder.ui.statebutton.rememberCombinedButtonState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioControlsLayout(
    modifier: Modifier,
    viewModel: AudioControlsViewModel = hiltViewModel()
) {
    val state: AudioControlsView.State by viewModel.state.collectAsState()
    val buttonSize = 54.dp
    val buttonSizeSmall = 34.dp
    val sendInput: (AudioControlsView.Input) -> Unit = remember { { viewModel.onEvent(it) } }
    val mapVisibility: (Boolean) -> Float = { if (it) 1f else 0f }
    val permissionsState =
        rememberPermissionState(permission = "android.permission.RECORD_AUDIO") { granted ->
            if (granted) {
                sendInput(AudioControlsView.Input.Record)
            }
        }
    val playButtonState = rememberCombinedButtonState(state.playButtonState)
    val recordButtonState = rememberCombinedButtonState(state.recordButtonState)
    val stopButtonState = rememberCombinedButtonState(state.stopButtonState)
    val shareButtonState = rememberCombinedButtonState(state.shareButtonState)

    Card(
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            CounterText(
                align = TextAlign.End,
                text = state.recordingDuration?.toDisplayCompose()
                    ?: state.playerProgressState?.data?.from.toDisplayCompose(),
                modifier = Modifier
                    .weight(1f),
                alpha = mapVisibility(state.playerProgressState != null || state.recordingDuration != null)
            )
            Row(
                Modifier
                    .weight(1.7f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                PlayButton(
                    state = playButtonState,
                    onClick = { sendInput(AudioControlsView.Input.Play) },
                    Modifier.size(buttonSizeSmall)
                )
                RecordButton(
                    state = recordButtonState,
                    onClick = {
                        permissionsState.launchPermissionRequest()
                    },
                    Modifier
                        .size(buttonSize)
                        .padding(0.dp)
                )
                StopButton(
                    state = stopButtonState,
                    onClick = { sendInput(AudioControlsView.Input.Stop) },
                    Modifier.size(buttonSizeSmall)
                )
                ShareButton(
                    state = shareButtonState,
                    onClick = { sendInput(AudioControlsView.Input.Share) },
                    Modifier.size(buttonSizeSmall)
                )
            }
            CounterText(
                modifier = Modifier
                    .weight(1f),
                align = TextAlign.Start,
                text = state.playerProgressState?.data?.to.toDisplayCompose(),
                alpha = mapVisibility(state.playerProgressState != null)
            )
        }
    }
}


@Composable
private fun CounterText(
    text: AnnotatedString,
    alpha: Float,
    align: TextAlign,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        fontStyle = MaterialTheme.typography.titleSmall.fontStyle,
        textAlign = align,
        modifier = modifier
            .alpha(alpha)
            .padding(horizontal = textPadding)
    )
}

val textPadding = 8.dp
