package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.domain.FeatureFlagSetting
import com.omar.retromp3recorder.domain.FeatureFlagsCollection
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeatureMapSaveUCTest {
    private lateinit var featureFlagRepo: FeatureFlagRepo
    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private lateinit var useCase: FeatureMapSaveUC

    @Before
    fun setUp() {
        featureFlagRepo =
            FeatureFlagRepo().apply { this.onNext(FeatureFlagsCollection(emptyMap())) }
        useCase = FeatureMapSaveUC(featureFlagRepo, sharedPreferences)

        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.apply() } returns Unit
    }

    @Test
    fun `put feature update into shared prefs`() = runTest {
        val flag = FeatureFlag.LogView
        val setting = FeatureFlagSetting(true)

        useCase.execute(flag, setting)

        verifyOrder {
            editor.putBoolean(FeatureFlag.LogView.key, true)
            editor.apply()
        }
    }

    @Test
    fun `put feature update into flag repo`() = runTest {
        val flag = FeatureFlag.LogView
        val setting = FeatureFlagSetting(true)

        useCase.execute(flag, setting)

        val first = featureFlagRepo.flow().first()
        assertEquals(first.featuresMap[flag], setting)
    }

    @Test
    fun `put replaced same flag with update in repo`() = runTest {
        val flag = FeatureFlag.LogView
        val setting = FeatureFlagSetting(true)
        val updateSetting = FeatureFlagSetting(false)

        featureFlagRepo.onNext(FeatureFlagsCollection(mapOf(flag to setting)))


        useCase.execute(flag, updateSetting)

        val first = featureFlagRepo.flow().first()
        assertEquals(first.featuresMap[flag], updateSetting)
    }

}
