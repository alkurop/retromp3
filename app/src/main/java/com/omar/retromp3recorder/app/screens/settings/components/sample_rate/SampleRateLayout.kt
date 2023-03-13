package com.omar.retromp3recorder.app.screens.settings.components.sample_rate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.settings.components.components.selection_group.SelectionGroup
import com.omar.retromp3recorder.app.screens.settings.components.components.selection_group.SelectionGroupData
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder

@Composable
fun SampleRateLayout(
    modifier: Modifier = Modifier,
    viewModel: SampleRateViewModelFlow = viewModel(),
) {
    val state by viewModel.state.collectAsState()
    val title = stringResource(id = R.string.sample_rate)
    val onSelected: (Mp3VoiceRecorder.SampleRate) -> Unit = remember {
        {
            viewModel.onEvent(it)
        }
    }
    val settings = SelectionGroupData(
        title = title,
        options = Mp3VoiceRecorder.SampleRate.values()
            .map { stringResource(R.string.sample_rate_format, it.value) },
        selection = Mp3VoiceRecorder.SampleRate.values().indexOf(state)
    )
    SelectionGroup(settings = settings, modifier = modifier, onSelected = {
        onSelected(Mp3VoiceRecorder.SampleRate.values()[it])
    })
}
