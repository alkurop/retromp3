package com.omar.retromp3recorder.app.screens.main.components.visualizer

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.bl.audio.PlayerIdMapper
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VisualizerInteractorFlowTest {
    private val playerIdMapper = mockk<PlayerIdMapper>(relaxed = true)
    private val audioStateMapper = mockk<AudioStateMapper>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var tested: VisualizerInteractorFlow

    @Before
    fun setUp() {
        tested = VisualizerInteractorFlow(playerIdMapper, audioStateMapper, dispatcher)
    }

    @Test
    fun `listens to audio state mapper`() = runTest {
        val state = AudioState.Playing
        every { audioStateMapper.observe() } returns Observable.just(state)

        tested.processIO().test {
            val item = awaitItem() as VisualizerView.Output.AudioStateChanged
            assertEquals(state, item.state)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `listens to player id mapper`() = runTest {
        val playerId = 10
        every { playerIdMapper.flow() } returns flowOf(playerId)

        tested.processIO().test {
            val item = awaitItem() as VisualizerView.Output.PlayerIdOutput
            assertEquals(playerId, item.playerId)
        }
    }
}
