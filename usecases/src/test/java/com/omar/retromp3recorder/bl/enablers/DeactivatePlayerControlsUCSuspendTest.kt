package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DeactivatePlayerControlsUCSuspendTest {
    lateinit var playerControlsRepo: PlayerControlsRepo

    @InjectMockKs
    lateinit var subject: DeactivatePlayerControlsUCSuspend

    @Before
    fun setUp() {
        playerControlsRepo = PlayerControlsRepo()
        MockKAnnotations.init(this)
    }

    @Test
    fun `execute should deactivate player controls`() = runBlocking {
        // Given
        val loop = PlayerControls.LoopSettings(isEnabled = true)
        val range = PlayerControls.RangeSettings(isVisible = true)
        val reverse = PlayerControls.ReverseSettings(isEnabled = true)
        val speed = PlayerControls.SpeedSettings(isEnabled = true)
        playerControlsRepo.emit(PlayerControls(loop, range, reverse, speed))
        val expected = PlayerControls(
            loop.copy(isEnabled = false),
            range.copy(isVisible = false),
            reverse.copy(isEnabled = false),
            speed.copy(isEnabled = false)
        )
        // When
        subject.execute()
        // Then
        val result = playerControlsRepo.first()
        assertEquals(expected, result)
    }
}
