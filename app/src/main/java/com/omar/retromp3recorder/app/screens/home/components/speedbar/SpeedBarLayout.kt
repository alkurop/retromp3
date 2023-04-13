package com.omar.retromp3recorder.app.screens.home.components.speedbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.home.components.Component
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun SpeedBarLayout(
    modifier: Modifier = Modifier,
    viewModel: SpeedBarViewModel = hiltViewModel(),
) {

    val state by viewModel.state.collectAsState()
    if (state is SpeedBarContract.State.Visible) {
        val visibleState = state as SpeedBarContract.State.Visible
        var rangeState by remember {
            mutableStateOf(visibleState.speed.speed.roundTo(1))
        }
        val active = visibleState.speed.isEnabled
        val rangeStateText = stringResource(if (active) R.string.on else R.string.off)
        val rangeText = stringResource(R.string.speed_enabled, "$rangeStateText ${rangeState}x")

        val sendRangeUpdate: (Float) -> Unit = {
            rangeState = it.roundTo(1)
            viewModel.onEvent(SpeedBarContract.Input.SpeedSet(it))
        }

        Component(
            modifier = modifier
        ) {
            Slider(
                modifier = Modifier.padding(horizontal = 8.dp),
                value = rangeState,
                valueRange = 0.2f..4f,
                onValueChange = sendRangeUpdate,
                steps = 10,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.tertiary,
                )
            )
            Row(
                Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { viewModel.onEvent(SpeedBarContract.Input.Enable) }
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.min_speed))
                Text(text = rangeText)
                Text(text = stringResource(id = R.string.max_speed))
            }
        }
    }
}

fun Float.roundTo(numFractionDigits: Int): Float {
    val factor = 10.0.pow(numFractionDigits)
    return (this * factor).roundToInt() / factor.toFloat()
}
