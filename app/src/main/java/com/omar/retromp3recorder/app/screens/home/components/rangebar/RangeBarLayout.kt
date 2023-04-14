package com.omar.retromp3recorder.app.screens.home.components.rangebar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.home.components.Component
import com.omar.retromp3recorder.app.screens.home.components.rememberVisibilityState
import com.omar.retromp3recorder.app.utils.TimeDisplay.toDisplayCompose
import com.omar.retromp3recorder.domain.PlayerRange


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeBarLayout(
    modifier: Modifier = Modifier,
    viewModel: RangeBarViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val visibilityState = rememberVisibilityState()
    visibilityState.isVisible = state.isVisible
    Component(modifier, visibilityState) {
        val barContent = state.barContent
        if (barContent != null) {
            val range = barContent.range
            var rangeState by remember {
                mutableStateOf(range.toViewRange())
            }
            val active = barContent.isActive
            val rangeStateText = stringResource(if (active) R.string.on else R.string.off)
            val rangeText = stringResource(R.string.range_enabled, rangeStateText)

            val sendRangeUpdate: (ClosedFloatingPointRange<Float>) -> Unit = {
                rangeState = it
                viewModel.onEvent(
                    RangeBarContract.Input.RangeSet(
                        range.copyWithUpdate(it)
                    )
                )
            }
            RangeSlider(
                modifier = Modifier.padding(horizontal = 8.dp),
                value = rangeState,
                onValueChange = sendRangeUpdate,
                valueRange = range.toValueRange(),
                steps = range.max,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.tertiary,
                )
            )
            Row(
                Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { viewModel.onEvent(RangeBarContract.Input.Enable) }
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = barContent.fromToMillis.from.toDisplayCompose())
                Text(text = rangeText)
                Text(text = barContent.fromToMillis.to.toDisplayCompose())
            }
        }
    }
}

private fun PlayerRange.copyWithUpdate(update: ClosedFloatingPointRange<Float>): PlayerRange =
    this.copy(
        from = update.start.toInt(),
        to = update.endInclusive.toInt()
    )

private fun PlayerRange.toViewRange(): ClosedFloatingPointRange<Float> =
    this.from.toFloat().rangeTo(this.to.toFloat())

private fun PlayerRange.toValueRange(): ClosedFloatingPointRange<Float> =
    0f.rangeTo(this.max.toFloat())
