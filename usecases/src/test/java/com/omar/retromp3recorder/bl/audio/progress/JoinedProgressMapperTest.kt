package com.omar.retromp3recorder.bl.audio.progress

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.waveform.RecordWavetableMapper
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.domain.PlayerProgress
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.toOptional
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class JoinedProgressMapperTest {
    private val audioStateMapper = mockk<AudioStateMapper>(relaxed = true)
    private lateinit var currentFileRepo: CurrentFileRepo
    private val playerProgressRepo = mockk<PlayerProgressRepo>(relaxed = true)
    private val recorderWavetableMapper = mockk<RecordWavetableMapper>(relaxed = true)
    private lateinit var tested: JoinedProgressMapper

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = JoinedProgressMapper(
            audioStateMapper,
            currentFileRepo,
            playerProgressRepo,
            recorderWavetableMapper
        )
    }

    @Test
    fun `When Playing has progress has file THEN progress shown`() = runTest {
        val file = MockFileFactory.giveExistingFile()

        every { playerProgressRepo.flow() } returns flowOf(mockk<PlayerProgress>().toOptional())
        every { audioStateMapper.flow() } returns flowOf(AudioState.Playing)

        currentFileRepo.emit(file.toOptional())

        tested.flow().test {
            val item = awaitItem()
            assert(item is JoinedProgress.PlayerProgressShown)
        }
    }

    @Test
    fun `When Idle has file THEN progress shown`() = runTest {
        val file = MockFileFactory.giveExistingFile()

        every { playerProgressRepo.flow() } returns flowOf(mockk<PlayerProgress>().toOptional())
        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)

        currentFileRepo.emit(file.toOptional())

        tested.flow().test {
            val item = awaitItem()
            assert(item is JoinedProgress.PlayerProgressShown)
        }
    }

    @Test
    fun `When Idle no progress then hidden`() = runTest {
        val file = MockFileFactory.giveExistingFile()

        every { playerProgressRepo.flow() } returns flowOf(Optional.empty())
        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)

        currentFileRepo.emit(file.toOptional())

        tested.flow().test {
            val item = awaitItem()
            assert(item is JoinedProgress.Hidden)
        }
    }

    @Test
    fun `When Idle not file THEN hidden`() = runTest {

        every { playerProgressRepo.flow() } returns flowOf(mockk<PlayerProgress>().toOptional())
        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)

        tested.flow().test {
            val item = awaitItem()
            assert(item is JoinedProgress.Hidden)
        }
    }

    @Test
    fun `When Idle future file THEN intermediate`() = runTest {
        val file = MockFileFactory.giveFutureFile()
        currentFileRepo.emit(file.toOptional())

        every { playerProgressRepo.flow() } returns flowOf(mockk<PlayerProgress>().toOptional())
        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)

        tested.flow().test {
            val item = awaitItem()
            assert(item is JoinedProgress.Intermediate)
        }
    }

    @Test
    fun `When Recording then recorder progress shown`() = runTest {

        every { playerProgressRepo.flow() } returns flowOf(mockk<PlayerProgress>().toOptional())
        every { audioStateMapper.flow() } returns flowOf(AudioState.Recording)

        tested.flow().test {
            val item = awaitItem()
            assert(item is JoinedProgress.RecorderProgressShown)
            awaitComplete()
        }
    }

}
