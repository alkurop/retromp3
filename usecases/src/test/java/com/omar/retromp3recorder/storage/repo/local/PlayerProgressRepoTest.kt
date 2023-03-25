package com.omar.retromp3recorder.storage.repo.local

import app.cash.turbine.test
import com.omar.retromp3recorder.data.mock.MockPlayerProgressFactory
import com.omar.retromp3recorder.domain.PlayerControls
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerProgressRepoTest {
    private lateinit var playerControlsRepo: PlayerControlsRepo
    private lateinit var tested: PlayerProgressRepo
    private val progressMapper = mockk<PlayerProgressMapperFlow>()
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        playerControlsRepo = PlayerControlsRepo()
        tested = PlayerProgressRepo(playerControlsRepo, progressMapper, dispatcher)
    }


    @Test
    fun `player controls range settings copied`() = runTest {
        val initial = MockPlayerProgressFactory.giveRange()
        val range1 = initial.copy(settings = initial.settings.copy(isActive = true))
        val progress = MockPlayerProgressFactory.givePlayerProgress()

        playerControlsRepo.emit(PlayerControls(rangeSettings = range1.settings))
        tested.emit(PlayerProgressRepo.In.Progress(progress))

        tested.flow().test {
            val item = awaitItem()
            assertEquals(range1.settings, item.value?.range?.settings)
        }
    }

    @Test
    fun `in new file updates progress`() = runTest {
        val progress = MockPlayerProgressFactory.givePlayerProgress().copy(progress = 28)

        tested.emit(PlayerProgressRepo.In.NewCurrentFile(progress))

        tested.flow().test {
            val item = awaitItem()
            assertEquals(progress, item.value)
        }
    }

    @Test
    fun `in range updates range if current not empty`() = runTest {
        val range = MockPlayerProgressFactory.giveRange().copy(from = 28)
        val progress = MockPlayerProgressFactory.givePlayerProgress().copy(progress = 0)

        tested.emit(PlayerProgressRepo.In.NewCurrentFile(progress))
        tested.emit(PlayerProgressRepo.In.Range(range))

        tested.flow().test {
            val item = awaitItem()
            assertEquals(range, item.value?.range)
        }
    }

    @Test
    fun `in progress takes last range`() = runTest {
        val range = MockPlayerProgressFactory.giveRange().copy(from = 28)
        val progress = MockPlayerProgressFactory.givePlayerProgress().copy(progress = 0)

        tested.emit(PlayerProgressRepo.In.NewCurrentFile(progress))
        tested.emit(PlayerProgressRepo.In.Range(range))
        tested.emit(PlayerProgressRepo.In.Progress(progress))

        tested.flow().test {
            val item = awaitItem()
            assertEquals(range, item.value?.range)
        }
    }

    @Test
    fun `in seek updates progress`() = runTest {
        val progress = MockPlayerProgressFactory.givePlayerProgress().copy(progress = 0)

        tested.emit(PlayerProgressRepo.In.NewCurrentFile(progress))
        val progressUpdate: Long = 28
        tested.emit(PlayerProgressRepo.In.Seek(progressUpdate))

        tested.flow().test {
            val item = awaitItem()
            assertEquals(progressUpdate, item.value?.progress)
        }
    }
}
