package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import app.cash.turbine.test
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.PlayButtonStateFlow
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.RecordButtonStateFlow
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.ShareButtonStateFlow
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate.StopButtonStateMapperFlow
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.actions.StartPlaybackUC
import com.omar.retromp3recorder.bl.audio.actions.StartRecordUC
import com.omar.retromp3recorder.bl.audio.actions.StopPlaybackAndRecordUC
import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
import com.omar.retromp3recorder.bl.system.ShareUC
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.domain.PlayerProgress
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AudioControlsInteractorFlowTest {

    private val playButtonStateMapper: PlayButtonStateFlow = mockk()
    private val joinedProgressMapper: JoinedProgressMapper = mockk()
    private val recordButtonStateMapper: RecordButtonStateFlow = mockk()
    private val recorderDurationStateFlow: RecorderDurationStateFlow = mockk()
    private val shareButtonStateMapper: ShareButtonStateFlow = mockk()
    private val stopButtonStateMapper: StopButtonStateMapperFlow = mockk()

    private val startRecordUC: StartRecordUC = mockk(relaxed = true)
    private val shareUC: ShareUC = mockk(relaxed = true)
    private val startPlaybackUC: StartPlaybackUC = mockk(relaxed = true)
    private val stopPlaybackAndRecordUC: StopPlaybackAndRecordUC = mockk(relaxed = true)

    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var tested: AudioControlsInteractorFlow

    @Before
    fun setUp() {
        every { playButtonStateMapper.flow() } returns emptyFlow()
        every { joinedProgressMapper.flow() } returns flowOf()
        every { recordButtonStateMapper.flow() } returns emptyFlow()
        every { recorderDurationStateFlow.flow() } returns emptyFlow()
        every { shareButtonStateMapper.flow() } returns emptyFlow()
        every { stopButtonStateMapper.flow() } returns emptyFlow()

        tested = AudioControlsInteractorFlow(
            playButtonStateMapper,
            joinedProgressMapper,
            recordButtonStateMapper,
            recorderDurationStateFlow,
            shareButtonStateMapper,
            stopButtonStateMapper,
            startRecordUC,
            shareUC,
            startPlaybackUC,
            stopPlaybackAndRecordUC,
            dispatcher
        )
    }

    @Test
    fun `listen record duration state`() = runTest {
        val expected = mockk<AudioControlsView.Output.RecorderDurationState>()

        every { recorderDurationStateFlow.flow() } returns flowOf(expected)

        tested.processIO(emptyFlow()).test {
            val item = awaitItem() as AudioControlsView.Output.RecorderDurationState
            assertEquals(expected, item)
        }
    }

    @Test
    fun `listen joined progress state`() = runTest {
        val progressExpected = mockk<PlayerProgress>()
        val mockData = mockk<JoinedProgress.PlayerProgressShown>()

        val expected = AudioControlsView.Output.PlayerProgressState(progressExpected)

        every { mockData.progress } returns progressExpected
        every { joinedProgressMapper.flow() } returns flowOf(mockData)

        tested.processIO(emptyFlow()).test {
            val item = awaitItem() as AudioControlsView.Output.PlayerProgressState
            assertEquals(expected, item)
            assertEquals(progressExpected, item.state)
            awaitComplete()
        }
    }

    @Test
    fun `listen play button state`() = runTest {
        val expected = InteractiveButtonState.RUNNING

        every { playButtonStateMapper.flow() } returns flowOf(expected)

        tested.processIO(emptyFlow()).test {
            val item = awaitItem() as AudioControlsView.Output.PlayButtonState
            assertEquals(expected, item.state)
        }
    }

    @Test
    fun `listen record button state`() = runTest {
        val expected = InteractiveButtonState.RUNNING

        every { recordButtonStateMapper.flow() } returns flowOf(expected)

        tested.processIO(emptyFlow()).test {
            val item = awaitItem() as AudioControlsView.Output.RecordButtonState
            assertEquals(expected, item.state)
        }
    }

    @Test
    fun `listen share button state`() = runTest {
        val expected = InteractiveButtonState.RUNNING

        every { shareButtonStateMapper.flow() } returns flowOf(expected)

        tested.processIO(emptyFlow()).test {
            val item = awaitItem() as AudioControlsView.Output.ShareButtonState
            assertEquals(expected, item.state)
        }
    }

    @Test
    fun `listen stop button state`() = runTest {
        val expected = InteractiveButtonState.DISABLED

        every { stopButtonStateMapper.flow() } returns flowOf(expected)

        tested.processIO(emptyFlow()).test {
            val item = awaitItem() as AudioControlsView.Output.StopButtonState
            assertEquals(expected, item.state)
        }
    }

    @Test
    fun `play input executes correct usecase`() = runTest {
        tested.processIO(flowOf(AudioControlsView.Input.Play)).test {}
        verify(exactly = 1) { startPlaybackUC.execute() }
        verify(exactly = 0) { stopPlaybackAndRecordUC.execute() }
        verify(exactly = 0) { startRecordUC.execute() }
        verify(exactly = 0) { shareUC.execute() }
    }

    @Test
    fun `record input executes correct usecase`() = runTest {
        tested.processIO(flowOf(AudioControlsView.Input.Record)).test {}

        verify(exactly = 0) { startPlaybackUC.execute() }
        verify(exactly = 0) { stopPlaybackAndRecordUC.execute() }
        verify(exactly = 1) { startRecordUC.execute() }
        verify(exactly = 0) { shareUC.execute() }
    }

    @Test
    fun `stop input executes correct usecase`() = runTest {
        tested.processIO(flowOf(AudioControlsView.Input.Stop)).test {}

        verify(exactly = 0) { startPlaybackUC.execute() }
        verify(exactly = 1) { stopPlaybackAndRecordUC.execute() }
        verify(exactly = 0) { startRecordUC.execute() }
        verify(exactly = 0) { shareUC.execute() }
    }

    @Test
    fun `share input executes correct usecase`() = runTest {
        tested.processIO(flowOf(AudioControlsView.Input.Share)).test {}

        verify(exactly = 0) { startPlaybackUC.execute() }
        verify(exactly = 0) { stopPlaybackAndRecordUC.execute() }
        verify(exactly = 0) { startRecordUC.execute() }
        verify(exactly = 1) { shareUC.execute() }
    }

}
