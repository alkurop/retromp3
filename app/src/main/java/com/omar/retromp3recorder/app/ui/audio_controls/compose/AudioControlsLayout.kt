package com.omar.retromp3recorder.app.ui.audio_controls.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsView
import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsViewModel
import com.omar.retromp3recorder.app.ui.theme.RetroTheme

@Composable
fun AudioControlsLayout(viewModel: AudioControlsViewModel = viewModel(), modifier: Modifier) {

    val state: AudioControlsView.State by viewModel.state.subscribeAsState(initial = AudioControlsView.State())

    val textWidth = 64.dp
    val textPadding = 8.dp
    val buttonSize = 32.dp
    val spacerWeight = 0.1f

    val sendInput: (AudioControlsView.Input) -> Unit = { viewModel.input.onNext(it) }

    val mapVisibility: (Boolean) -> Float = { if (it) 1f else 0f }

    RetroTheme {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.weight(spacerWeight))
            Text(
                text = "00",
                textAlign = TextAlign.End,
                modifier = Modifier
                    .width(textWidth)
                    .alpha(mapVisibility(state.playerProgressState != null))
                    .padding(horizontal = textPadding)
            )
            Row(
                Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                PlayButton(
                    state = state.playButtonState,
                    onClick = { sendInput(AudioControlsView.Input.Play) },
                    Modifier.width(buttonSize)
                )
                RecordButton(
                    state = state.recordButtonState,
                    onClick = { sendInput(AudioControlsView.Input.Record) },
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
                text = "asd",
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

//private fun AudioControlsView.State.progressText(context: Context): String? {
//
//        state.playerProgressState?.apply {
//        progressView.text = data.from.toDisplay(requireContext())
//        durationView.text = data.to.toDisplay(requireContext())
//    }
//    state.recordingDuration?.let {
//        durationView.text = it.toDisplay(requireContext())
////    }
//val res = this.playerProgressState?.let {
//    it.data.from.toDisplay(context)
//}
//}
//
//
//private fun AudioControlsView.State.durationText(): String? {
//}
//



