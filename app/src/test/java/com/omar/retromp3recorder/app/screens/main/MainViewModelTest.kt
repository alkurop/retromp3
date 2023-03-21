package com.omar.retromp3recorder.app.screens.main

import android.media.projection.MediaProjection
import app.cash.turbine.test
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.bl.audio.UpdateMediaProjectionUC
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.domain.FeatureFlagSetting
import com.omar.retromp3recorder.domain.FeatureFlagsCollection
import com.omar.retromp3recorder.domain.platform.MediaProjectionState
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var tested: MainViewModel
    private lateinit var mediaProjectionStateRepo: MediaProjectionStateRepo
    private val usecase = mockk<UpdateMediaProjectionUC>(relaxed = true)
    private lateinit var featureFlagRepo: FeatureFlagRepo
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mediaProjectionStateRepo = MediaProjectionStateRepo()
        featureFlagRepo = FeatureFlagRepo()
        tested = MainViewModel(mediaProjectionStateRepo, usecase, featureFlagRepo, dispatcher)
    }

    @After
    fun clear() {
        Dispatchers.resetMain()
    }

    @Test
    fun `listens to feature flag repo`() = runTest {
        featureFlagRepo.emit(
            FeatureFlagsCollection(
                mapOf(
                    FeatureFlag.LogView to FeatureFlagSetting(true),
                    FeatureFlag.KeepScreenOn to FeatureFlagSetting(true),
                )
            )
        )
        tested.state.test {
            val item = awaitItem()
            val isLogView = item.isLogViewEnabled
            val isKeepScreen = item.shouldKeepScreenOn
            assert(isLogView)
            assert(isKeepScreen)
        }
    }

    @Test
    fun `listens to media projection repo`() = runTest {
        val request = 234
        mediaProjectionStateRepo.emit(MediaProjectionState(request = Shell(request)))
        tested.state.test {
            val item = awaitItem()
            val result = item.requestForScreenCapture.ghost
            assertEquals(request, result)
        }
    }

    @Test
    fun `input projection executes usecase`() = runTest {
        val mediaProjection = mockk<MediaProjection>()
        tested.emit(MainViewContract.Input.MediaProjectionUpdated(mediaProjection))

        verify { usecase.execute(mediaProjection) }
    }
}
