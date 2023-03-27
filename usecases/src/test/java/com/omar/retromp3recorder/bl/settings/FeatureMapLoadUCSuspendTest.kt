package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.domain.FeatureFlagSetting
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("UNUSED")
class FeatureMapLoadUCSuspendTest {
    lateinit var featureFlagRepo: FeatureFlagRepo

    @MockK
    lateinit var sharedPreferences: SharedPreferences

    @InjectMockKs
    lateinit var tested: FeatureMapLoadUCSuspend

    @Before
    fun setUp() {
        featureFlagRepo = FeatureFlagRepo()
        MockKAnnotations.init(this)
        every { sharedPreferences.getBoolean(any(), any()) } returns true
    }

    @Test
    fun `for each key shared pref is pulled`() = runTest {
        tested.execute()
        FeatureFlag.values().forEach {
            verify { sharedPreferences.getBoolean(it.key, it.isEnabledByDefault) }
        }
    }

    @Test
    fun `result map emitted with all keys`() = runTest {
        tested.execute()

        val expected = FeatureFlag.values().associateWith { FeatureFlagSetting(true) }
        val result = featureFlagRepo.first().featuresMap
        assertEquals(expected, result)
    }
}
