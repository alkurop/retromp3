package com.omar.retromp3recorder.app.screens.settings.components.beat_rate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.settings.components.components.selection_group.SelectionGroup
import com.omar.retromp3recorder.app.screens.settings.components.components.selection_group.SelectionGroupData
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder

@Composable
fun BitRateLayout(
    modifier: Modifier = Modifier,
    viewModel: BitRateSettingsViewModel = viewModel(),
) {
    val state = viewModel.state.subscribeAsState(initial = Mp3VoiceRecorder.BitRate.values()[0])
    val title = stringResource(id = R.string.bit_rate)
    val onSelected: (Mp3VoiceRecorder.BitRate) -> Unit = remember {
        {
            viewModel.input.onNext(it)
        }
    }
    val settings = SelectionGroupData(
        title = title,
        options = Mp3VoiceRecorder.BitRate.values()
            .map { stringResource(R.string.bit_rate_format, it.value) },
        selection = Mp3VoiceRecorder.BitRate.values().indexOf(state.value)
    )
    SelectionGroup(settings = settings, modifier = modifier, onSelected = {
        onSelected(Mp3VoiceRecorder.BitRate.values()[it])
    })
}
