package com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.AudioControlsView
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.AudioControlsViewModelFlow
import com.omar.retromp3recorder.app.utils.TimeDisplay.toDisplayCompose

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioControlsLayout(
    modifier: Modifier,
    viewModel: AudioControlsViewModelFlow = viewModel()
) {

    val state: AudioControlsView.State by viewModel.state.collectAsState()

    val textWidth = 64.dp
    val textPadding = 8.dp
    val buttonSize = 32.dp
    val spacerWeight = 0.1f

    val sendInput: (AudioControlsView.Input) -> Unit = { viewModel.onEvent(it) }

    val mapVisibility: (Boolean) -> Float = { if (it) 1f else 0f }

    val permissionsState =
        rememberPermissionState(permission = "android.permission.RECORD_AUDIO") { granted ->
            if (granted) {
                sendInput(AudioControlsView.Input.Record)
            }
        }

    Card(
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = modifier
        ) {
            Spacer(modifier = Modifier.weight(spacerWeight))
            Text(
                text = state.playerProgressState?.data?.from.toDisplayCompose(),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .width(textWidth)
                    .alpha(mapVisibility(state.playerProgressState != null))
                    .padding(horizontal = textPadding)
            )
            Row(
                Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                PlayButton(
                    state = state.playButtonState,
                    onClick = { sendInput(AudioControlsView.Input.Play) },
                    Modifier.width(buttonSize)
                )
                RecordButton(
                    state = state.recordButtonState,
                    onClick = {
                        permissionsState.launchPermissionRequest()
                    },
                    Modifier.width(buttonSize)
                )
                StopButton(
                    state = state.stopButtonState,
                    onClick = { sendInput(AudioControlsView.Input.Stop) },
                    Modifier.width(buttonSize)
                )
                ShareButton(
                    state = state.shareButtonState,
                    onClick = { sendInput(AudioControlsView.Input.Share) },
                    Modifier.width(buttonSize)
                )
            }
            Text(
                text = state.recordingDuration?.toDisplayCompose()
                    ?: state.playerProgressState?.data?.to.toDisplayCompose(),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .width(textWidth)
                    .alpha(mapVisibility(state.playerProgressState != null || state.recordingDuration != null))
                    .padding(horizontal = textPadding)
            )
            Spacer(modifier = Modifier.weight(spacerWeight))
        }
    }
}
