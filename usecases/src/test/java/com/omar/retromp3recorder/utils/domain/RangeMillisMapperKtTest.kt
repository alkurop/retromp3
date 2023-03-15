package com.omar.retromp3recorder.utils.domain

import com.omar.retromp3recorder.data.mock.MockPlayerProgressFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RangeMillisMapperKtTest {

    @Test
    fun `from divided with duration`() = runTest {
        val range = MockPlayerProgressFactory.giveRange()
        val duration = 1000L

        val multiplier = duration / range.max

        val result = range.toFromToMillis(duration)

        val expected = range.from * multiplier

        assertEquals(result.from, expected)
    }

    @Test
    fun `from to with duration`() = runTest {
        val range = MockPlayerProgressFactory.giveRange()
        val duration = 1000L

        val multiplier = duration / range.max

        val result = range.toFromToMillis(duration)

        val expected = range.to * multiplier

        assertEquals(result.to, expected)
    }
}
