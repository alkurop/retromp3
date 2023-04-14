package com.omar.retromp3recorder.ui.wavetable.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.omar.retromp3recorder.domain.PlayerProgress
import com.omar.retromp3recorder.domain.Wavetable

@Immutable
data class WaveSeekData(
    val progress: PlayerProgress,
    val wavetable: Wavetable?,
)

@Composable
fun WavetableSeekbarCompose(
    data: WaveSeekData,
    onEvent: (SeekEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        data.wavetable?.let {
            WavetableCompose(
                modifier = Modifier.fillMaxSize(),
                data = WavetableComposeData(it)
            )
        }

        SeekBarCompose(
            modifier = Modifier.fillMaxSize(),
            onEvent = onEvent,
            data = SeekBarData(progress = data.progress)
        )
    }
}
