package com.omar.retromp3recorder.app.screens.settings.components.beat_rate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.settings.components.selection_group.SelectionGroup
import com.omar.retromp3recorder.app.screens.settings.components.selection_group.SelectionGroupData
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder

@Composable
fun BitRateLayout(
    modifier: Modifier = Modifier,
    viewModel: BitRateSettingsViewModelFlow = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val title = stringResource(id = R.string.bit_rate)
    val onSelected: (Mp3VoiceRecorder.BitRate) -> Unit = remember {
        {
            viewModel.onEvent(it)
        }
    }
    val settings = SelectionGroupData(
        title = title,
        options = Mp3VoiceRecorder.BitRate.values()
            .map { stringResource(R.string.bit_rate_format, it.value) },
        selection = Mp3VoiceRecorder.BitRate.values().indexOf(state)
    )
    SelectionGroup(settings = settings, modifier = modifier, onSelected = {
        onSelected(Mp3VoiceRecorder.BitRate.values()[it])
    })
}
