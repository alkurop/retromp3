package com.omar.retromp3recorder.app.screens.settings.flow

import app.cash.turbine.test
import com.omar.retromp3recorder.app.screens.settings.SettingsContract
import com.omar.retromp3recorder.bl.settings.FeatureMapSaveUC
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.domain.FeatureFlagSetting
import com.omar.retromp3recorder.domain.FeatureFlagsCollection
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsFlowInteractorTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val usecase = mockk<FeatureMapSaveUC>()
    private lateinit var repo: FeatureFlagRepo
    private lateinit var interactor: SettingsFlowInteractor

    @Before
    fun setUp() {
        repo = FeatureFlagRepo()
        coEvery { usecase.execute(any(), any()) } returns Unit
        interactor = SettingsFlowInteractor(usecase, repo, dispatcher)
    }

    @Test
    fun `on feature flag changed usecase executed`() = runTest {
        val event =
            SettingsContract.Input.FlagSettingChanged(
                FeatureFlag.LogView,
                FeatureFlagSetting(true)
            )
        interactor.processIO( flowOf(event)).test {
            expectNoEvents()
        }

        coVerify { usecase.execute(event.flag, event.setting) }
    }

    @Test
    fun `on flag repo update outputs new test map`() = runTest {
        val event =
            SettingsContract.Input.FlagSettingChanged(
                FeatureFlag.LogView,
                FeatureFlagSetting(true)
            )
        repo.emit(FeatureFlagsCollection(mapOf(event.flag to event.setting)))
        interactor.processIO(flowOf()).test {
            val item = awaitItem() as SettingsContract.Output.FlagsCollectionUpdate
            assertEquals(
                event.setting,
                item.featureFlagsCollection.featuresMap[FeatureFlag.LogView]
            )
        }
    }

}
