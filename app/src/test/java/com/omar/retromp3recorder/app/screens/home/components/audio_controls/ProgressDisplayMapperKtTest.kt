package com.omar.retromp3recorder.app.screens.home.components.audio_controls

import com.omar.retromp3recorder.data.mock.MockPlayerProgressFactory
import com.omar.retromp3recorder.domain.PlayerProgress
import com.omar.retromp3recorder.utils.domain.toFromToMillis
import org.junit.Assert.assertEquals
import org.junit.Test

class ProgressDisplayMapperKtTest {
    @Test
    fun `WHEN range IS active THEN length of progress is length of range`() {
        val range = MockPlayerProgressFactory.giveRange(isActive = true)
        val duration = 1005L
        val progress = 88L

        val playerProgress = PlayerProgress(
            range = range,
            progress = progress,
            duration = duration
        )

        val convertedRange = range.toFromToMillis(duration)
        val displayRange = playerProgress.toProgressDisplay()

        // range should shift and show 0 when file progress is start or range.
        assertEquals(displayRange.data.to, convertedRange.length)
    }

    @Test
    fun `WHEN range NOT active THEN length of progress is duration`() {
        val range = MockPlayerProgressFactory.giveRange(isActive = false)
        val duration = 1005L
        val progress = 88L

        val playerProgress = PlayerProgress(
            range = range,
            progress = progress,
            duration = duration
        )

        val displayRange = playerProgress.toProgressDisplay()

        assertEquals(duration, displayRange.data.to)
    }

    /**
     * Logic in this test is tricky and needs to be double checked
     */
    @Test
    fun `WHEN range IS active THEN progress minus range`() {
        val range = MockPlayerProgressFactory.giveRange(isActive = true)
        val duration = 1005L
        val progress = 600L

        val playerProgress = PlayerProgress(
            range = range,
            progress = progress,
            duration = duration
        )

        val convertedRange = range.toFromToMillis(duration)
        val resultRange = playerProgress.toProgressDisplay()

        val expectedProgress = progress - convertedRange.from
        assertEquals(expectedProgress, resultRange.data.from)
    }

    @Test
    fun `WHEN range NOT active THEN progress`() {
        val range = MockPlayerProgressFactory.giveRange(isActive = false)
        val duration = 1005L
        val progress = 600L

        val playerProgress = PlayerProgress(
            range = range,
            progress = progress,
            duration = duration
        )

        val resultRange = playerProgress.toProgressDisplay()

        assertEquals(progress, resultRange.data.from)
    }
}
