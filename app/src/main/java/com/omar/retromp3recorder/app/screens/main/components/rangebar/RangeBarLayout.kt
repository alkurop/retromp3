package com.omar.retromp3recorder.app.screens.main.components.rangebar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.utils.TimeDisplay.toDisplayCompose
import com.omar.retromp3recorder.domain.PlayerRange


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeBarLayout(
    viewModel: RangeBarViewModel = viewModel(),
    modifier: Modifier
) {

    val state by viewModel.state.subscribeAsState(initial = RangeBarView.State.Hidden)
    if (state is RangeBarView.State.Visible) {
        val visibleState = state as RangeBarView.State.Visible
        val range = visibleState.range
        var rangeState by remember {
            mutableStateOf(range.toViewRange())
        }
        val active = visibleState.isActive
        val rangeStateText = stringResource(if (active) R.string.on else R.string.off)
        val rangeText = stringResource(R.string.range_enabled, rangeStateText)

        val sendRangeUpdate: (ClosedFloatingPointRange<Float>) -> Unit = {
            rangeState = it
            viewModel.input.onNext(
                RangeBarView.Input.RangeSet(
                    range.copeWithUpdate(it)
                )
            )
        }

        Surface(
            modifier
                .fillMaxWidth()
                .wrapContentHeight()

        ) {
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
                    .padding(top = 45.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        viewModel.input.onNext(RangeBarView.Input.Enable)
                    }
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = visibleState.fromToMillis.from.toDisplayCompose(),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(text = rangeText, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = visibleState.fromToMillis.to.toDisplayCompose(),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

private fun PlayerRange.copeWithUpdate(update: ClosedFloatingPointRange<Float>): PlayerRange =
    this.copy(
        from = update.start.toInt(),
        to = update.endInclusive.toInt()
    )

private fun PlayerRange.toViewRange(): ClosedFloatingPointRange<Float> =
    this.from.toFloat().rangeTo(this.to.toFloat())

private fun PlayerRange.toValueRange(): ClosedFloatingPointRange<Float> =
    0f.rangeTo(this.max.toFloat())
