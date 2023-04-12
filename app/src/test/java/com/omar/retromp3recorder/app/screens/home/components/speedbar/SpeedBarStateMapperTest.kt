package com.omar.retromp3recorder.app.screens.home.components.speedbar

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SpeedBarStateMapperTest {

    private val joinedProgressMapper: JoinedProgressMapper = mockk()
    private lateinit var playerControlsRepo: PlayerControlsRepo
    private lateinit var tested: SpeedBarStateMapper

    @Before
    fun setup() {
        playerControlsRepo = PlayerControlsRepo()
        tested = SpeedBarStateMapper(joinedProgressMapper, playerControlsRepo)
    }

    @Test
    fun `when joined progress shown and speed visible then shown`() = runTest {
        every { joinedProgressMapper.flow() } returns flowOf(mockk<JoinedProgress.PlayerProgressShown>())
        playerControlsRepo.emit(
            PlayerControls(
                speedSettings = PlayerControls.SpeedSettings(
                    isVisible = true
                )
            )
        )

        tested.flow().test {
            val item = awaitItem()
            assert(item is SpeedBarContract.State.Visible)
        }
    }

    @Test
    fun `when joined progress shown and speed not visible then invisible`() = runTest {
        every { joinedProgressMapper.flow() } returns flowOf(mockk<JoinedProgress.PlayerProgressShown>())
        playerControlsRepo.emit(
            PlayerControls(
                speedSettings = PlayerControls.SpeedSettings(
                    isVisible = false
                )
            )
        )

        tested.flow().test {
            val item = awaitItem()
            assert(item is SpeedBarContract.State.Hidden)
        }
    }

    @Test
    fun `when joined progress record then hidden`() = runTest {
        every { joinedProgressMapper.flow() } returns flowOf(mockk<JoinedProgress.RecorderProgressShown>())
        playerControlsRepo.emit(
            PlayerControls(
                speedSettings = PlayerControls.SpeedSettings(
                    isVisible = true
                )
            )
        )

        tested.flow().test {
            val item = awaitItem()
            assert(item is SpeedBarContract.State.Hidden)
        }
    }

    @Test
    fun `when joined progress hidden then hidden`() = runTest {
        every { joinedProgressMapper.flow() } returns flowOf(mockk<JoinedProgress.Hidden>())
        playerControlsRepo.emit(
            PlayerControls(
                speedSettings = PlayerControls.SpeedSettings(
                    isVisible = true
                )
            )
        )

        tested.flow().test {
            val item = awaitItem()
            assert(item is SpeedBarContract.State.Hidden)
        }
    }
}
