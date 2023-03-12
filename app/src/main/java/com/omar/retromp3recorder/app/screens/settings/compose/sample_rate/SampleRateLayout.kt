package com.omar.retromp3recorder.app.screens.settings.compose.sample_rate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.settings.components.sample_rate.SampleRateViewModel
import com.omar.retromp3recorder.app.screens.settings.compose.group.SettingGroup
import com.omar.retromp3recorder.app.screens.settings.compose.group.SettingGroupData
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder

@Composable
fun SampleRateLayout(
    modifier: Modifier = Modifier,
    viewModel: SampleRateViewModel = viewModel(),
) {
    val state = viewModel.state.subscribeAsState(initial = Mp3VoiceRecorder.SampleRate.values()[0])
    val title = stringResource(id = R.string.sample_rate)
    val onSelected: (Mp3VoiceRecorder.SampleRate) -> Unit = remember {
        {
            viewModel.input.onNext(it)
        }
    }
    val settings = SettingGroupData(
        title = title,
        options = Mp3VoiceRecorder.SampleRate.values()
            .map { stringResource(R.string.sample_rate_format, it.value) },
        selection = Mp3VoiceRecorder.SampleRate.values().indexOf(state.value)
    )
    SettingGroup(settings = settings, modifier = modifier, onSelected = {
        onSelected(Mp3VoiceRecorder.SampleRate.values()[it])
    })
}
