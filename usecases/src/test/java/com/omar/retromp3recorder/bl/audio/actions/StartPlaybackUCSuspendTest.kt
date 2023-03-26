package com.omar.retromp3recorder.bl.audio.actions

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockPlayerProgressFactory
import com.omar.retromp3recorder.domain.FromToMillis
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import com.omar.retromp3recorder.utils.domain.toFromToMillis
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.toOptional
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StartPlaybackUCSuspendTest {
    private val audioPlayer = mockk<AudioPlayer>(relaxed = true)
    private lateinit var currentFileRepo: CurrentFileRepo
    private val playerProgressRepo = mockk<PlayerProgressRepo>()

    private lateinit var tested: StartPlaybackUCSuspend

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = StartPlaybackUCSuspend(audioPlayer, currentFileRepo, playerProgressRepo)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when no file then crash`() = runTest {
        every { playerProgressRepo.flow() } returns flowOf(Optional.empty())
        tested.execute()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when file future then crash`() = runTest {
        every { playerProgressRepo.flow() } returns flowOf(
            MockPlayerProgressFactory.givePlayerProgress().toOptional()
        )


        currentFileRepo.emit(MockFileFactory.giveFutureFile().toOptional())

        tested.execute()
    }


    @Test(expected = IllegalArgumentException::class)
    fun `when file length null then crash`() = runTest {
        every { playerProgressRepo.flow() } returns flowOf(
            MockPlayerProgressFactory.givePlayerProgress().toOptional()
        )

        currentFileRepo.emit(MockFileFactory.giveExistingFile().copy(length = null).toOptional())

        tested.execute()
    }

    @Test
    fun `when range active then playrange from range`() = runTest {
        val range = MockPlayerProgressFactory.giveRange(true)

        val givePlayerProgress = MockPlayerProgressFactory.givePlayerProgress(
            range = range
        )
        val giveExistingFile = MockFileFactory.giveExistingFile()

        val expected = range.toFromToMillis(giveExistingFile.length!!)

        currentFileRepo.emit(giveExistingFile.toOptional())

        every { playerProgressRepo.flow() } returns flowOf(
            givePlayerProgress.toOptional()
        )

        tested.execute()

        verify {
            audioPlayer.onInput(withArg {
                val start = requireNotNull(it as? AudioPlayer.Input.Start)

                val result = start.options.rangeMillis

                assertEquals(expected, result)
            })
        }
    }

    @Test
    fun `when range NOT active then playrange from progress and file length`() = runTest {
        val range = MockPlayerProgressFactory.giveRange(false)

        val givePlayerProgress = MockPlayerProgressFactory.givePlayerProgress(
            range = range
        )
        val giveExistingFile = MockFileFactory.giveExistingFile()

        val expected = FromToMillis(givePlayerProgress.progress, giveExistingFile.length!!)

        currentFileRepo.emit(giveExistingFile.toOptional())

        every { playerProgressRepo.flow() } returns flowOf(
            givePlayerProgress.toOptional()
        )

        tested.execute()

        verify {
            audioPlayer.onInput(withArg {
                val start = requireNotNull(it as? AudioPlayer.Input.Start)

                val result = start.options.rangeMillis

                assertEquals(expected, result)
            })
        }
    }

    @Test
    fun `range start reduced from relative progress`() = runTest {
        val range = MockPlayerProgressFactory.giveRange(true)

        val givePlayerProgress = MockPlayerProgressFactory.givePlayerProgress(
            range = range
        )
        val giveExistingFile = MockFileFactory.giveExistingFile().copy(length = 100)

        currentFileRepo.emit(giveExistingFile.toOptional())

        every { playerProgressRepo.flow() } returns flowOf(
            givePlayerProgress.toOptional()
        )

        tested.execute()

        verify {
            audioPlayer.onInput(withArg {
                val start = requireNotNull(it as? AudioPlayer.Input.Start)

                val result = start.options.relativeSeekPosition
                val expected =
                    givePlayerProgress.progress - range.toFromToMillis(giveExistingFile.length!!).from

                assertEquals(expected, result)
            })
        }
    }

    @Test
    fun `when progress over range then seek 0`() = runTest {
        val range = MockPlayerProgressFactory.giveRange(true)

        val givePlayerProgress = MockPlayerProgressFactory.givePlayerProgress(
            range = range
        )
        val giveExistingFile = MockFileFactory.giveExistingFile().copy(length = 200)

        currentFileRepo.emit(giveExistingFile.toOptional())

        every { playerProgressRepo.flow() } returns flowOf(
            givePlayerProgress.toOptional()
        )

        tested.execute()

        verify {
            audioPlayer.onInput(withArg {
                val start = requireNotNull(it as? AudioPlayer.Input.Start)

                val result = start.options.relativeSeekPosition
                val expected = 0L
                assertEquals(expected, result)
            })
        }
    }
}
