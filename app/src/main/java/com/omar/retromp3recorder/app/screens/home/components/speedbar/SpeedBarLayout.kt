package com.omar.retromp3recorder.app.screens.home.components.speedbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R

@Composable
fun SpeedBarLayout(
    modifier: Modifier = Modifier,
    viewModel: SpeedBarViewModel = hiltViewModel(),
) {

    val state by viewModel.state.collectAsState()
    if (state is SpeedBarContract.State.Visible) {
        val visibleState = state as SpeedBarContract.State.Visible
        var rangeState by remember {
            mutableStateOf(visibleState.speed.speed)
        }
        val active = visibleState.speed.isEnabled
        val rangeStateText = stringResource(if (active) R.string.on else R.string.off)
        val rangeText = stringResource(R.string.speed_enabled, rangeStateText)

        val sendRangeUpdate: (Float) -> Unit = {
            rangeState = it
            viewModel.onEvent(SpeedBarContract.Input.SpeedSet(it))
        }

        Surface(
            modifier
                .fillMaxWidth()
                .wrapContentHeight()

        ) {
            Slider(
                modifier = Modifier.padding(horizontal = 8.dp),
                value = rangeState,
                onValueChange = sendRangeUpdate,
                steps = 10,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.tertiary,
                )
            )
            Row(
                Modifier
                    .padding(top = 45.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        viewModel.onEvent(SpeedBarContract.Input.Enable)
                    }
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = rangeText, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
