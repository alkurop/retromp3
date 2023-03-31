package com.omar.retromp3recorder.app.screens.main

import android.media.projection.MediaProjection
import app.cash.turbine.test
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.bl.audio.record.UpdateMediaProjectionUC
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.domain.FeatureFlagSetting
import com.omar.retromp3recorder.domain.FeatureFlagsCollection
import com.omar.retromp3recorder.domain.platform.MediaProjectionState
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewInteractorTest {
    private lateinit var tested: MainViewInteractor
    private lateinit var collectorProjectionRepo: MutableStateFlow<MediaProjectionState>

    private val mediaProjectionStateRepo = mockk<MediaProjectionStateRepo>()
    private val usecase = mockk<UpdateMediaProjectionUC>(relaxed = true)
    private lateinit var featureFlagRepo: FeatureFlagRepo
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        collectorProjectionRepo = MutableStateFlow(MediaProjectionState())
        every { mediaProjectionStateRepo.flow() } coAnswers { collectorProjectionRepo }
        coEvery { mediaProjectionStateRepo.emit(any()) } coAnswers {
            collectorProjectionRepo.emit(it.invocation.args[0] as MediaProjectionState)
        }
        Dispatchers.setMain(dispatcher)
        featureFlagRepo = FeatureFlagRepo()
        tested = MainViewInteractor(mediaProjectionStateRepo, usecase, featureFlagRepo, dispatcher)
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
        tested.processIO(flowOf()).test {
            val item = awaitItem() as? MainViewContract.Output.SettingsUpdated
            val logFlag =
                item?.featureFlagsCollection?.featuresMap?.get(FeatureFlag.LogView)?.isEnabled
            val screenFlag =
                item?.featureFlagsCollection?.featuresMap?.get(FeatureFlag.KeepScreenOn)?.isEnabled

            assertEquals(true, logFlag)
            assertEquals(true, screenFlag)
        }
    }

    @Test
    fun `listens to media projection repo`() = runTest {
        val request = 234
        mediaProjectionStateRepo.emit(MediaProjectionState(request = Shell(request)))
        tested.processIO(flowOf()).test {
            val item = awaitItem() as MainViewContract.Output.RequestScreenCapture
            assertEquals(request, item.shouldRequest.ghost)
        }
    }

    @Test
    fun `input projection executes usecase`() = runTest {
        val mediaProjection = mockk<MediaProjection>()
        tested.processIO(flowOf(MainViewContract.Input.MediaProjectionUpdated(mediaProjection)))
            .test { }
        coVerify { usecase.execute(mediaProjection) }
    }
}
