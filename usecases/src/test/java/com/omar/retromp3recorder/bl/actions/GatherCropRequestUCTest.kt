package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockPlayerProgressFactory
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.Optional
import com.omar.retromp3recorder.utils.platform.toOptional
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GatherCropRequestUCTest {

    private lateinit var currentFileRepo: CurrentFileRepo
    private val joinedProgress = mockk<JoinedProgressMapper>()

    private lateinit var tested: GatherCropRequestUC
    private val nameSuggestion = mockk<NewNameSuggestion>()

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = GatherCropRequestUC(currentFileRepo, joinedProgress)
    }


    @Test(expected = IllegalArgumentException::class)
    fun `WHEN no file THEN crash`() = runTest {
        val progress = MockPlayerProgressFactory.givePlayerProgress()

        currentFileRepo.emit(Optional.empty())
        every { joinedProgress.observe() } returns Observable.just(
            JoinedProgress.PlayerProgressShown(
                progress,
                null
            )
        )
        tested.execute(nameSuggestion)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `WHEN player NOT in progress THEN crash`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveFile().toOptional())
        every { joinedProgress.observe() } returns Observable.just(
            JoinedProgress.Intermediate
        )
        tested.execute(nameSuggestion)
    }

    @Test
    fun `WHEN has file and has player progress THEN happy path`() = runTest {
        val progress = MockPlayerProgressFactory.givePlayerProgress()

        currentFileRepo.emit(MockFileFactory.giveFile().toOptional())
        every { joinedProgress.observe() } returns Observable.just(
            JoinedProgress.PlayerProgressShown(progress, null)
        )
        val result = tested.execute(nameSuggestion)
        assertNotNull(result)
    }
}
