package com.omar.retromp3recorder.app.screens.home.components.visualizer

import android.media.audiofx.Visualizer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.ui.wave_visualiser.VisualizerDisplayView
import timber.log.Timber

@Composable
fun VisualizerLayout(
    modifier: Modifier = Modifier,
    viewModel: VisualizerViewModelFlow = hiltViewModel()
) {
    val state: VisualizerView.State by viewModel.state.collectAsState()
    var visualizer: Visualizer? = null
    fun stopVisualizer() {
        visualizer?.enabled = false
        visualizer?.release()
        visualizer = null
    }
    Surface(modifier = modifier) {
        AndroidView(modifier = Modifier.fillMaxSize(),
            factory = { context -> VisualizerDisplayView(context) },
            update = { view ->
                val visualizerListener =
                    object : Visualizer.OnDataCaptureListener {
                        override fun onWaveFormDataCapture(
                            visualizer: Visualizer,
                            bytes: ByteArray,
                            samplingRate: Int
                        ) {
                            val input = bytes.map { it + Byte.MAX_VALUE }
                                .map { it.toByte() }.toByteArray()
                            view.updateVisualizer(input)
                        }

                        override fun onFftDataCapture(
                            visualizer: Visualizer,
                            bytes: ByteArray,
                            samplingRate: Int
                        ) = Unit
                    }
                val playerId = state.getID()
                if (playerId != null) {
                    try {
                        stopVisualizer()
                        visualizer = Visualizer(playerId).apply {
                            captureSize = Visualizer.getCaptureSizeRange()[1]

                            setDataCaptureListener(
                                visualizerListener,
                                Visualizer.getMaxCaptureRate() / 2,
                                true,
                                false
                            )
                            enabled = true

                        }
                    } catch (exception: Exception) {
                        Timber.e(exception)
                    }
                }

            })
        DisposableEffect(key1 = visualizer) {
            onDispose {
                stopVisualizer()
            }
        }
    }

}

private fun VisualizerView.State.getID(): Int? {
    val playerId = this.playerId
    return if (this.audioState in listOf(
            AudioState.Playing,
            AudioState.SeekPaused
        )
    ) playerId
    else null
}
