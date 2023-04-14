package com.omar.retromp3recorder.io.billing.connection

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConnectUCTest {
    private val tested = ConnectUC()
    private val connection = mockk<BillingConnection>(relaxed = true)

    @Test
    fun `on execute connection connect triggered`() = runTest {
        every { connection.connectionFlow } returns flowOf(BillingConnectionState.Connected)
        tested.execute(connection)

        verify { connection.connect() }
    }

    @Test(expected = NoSuchElementException::class)
    fun `when connection loading skip`() = runTest {
        every { connection.connectionFlow } returns flowOf(
            BillingConnectionState.Loading,
        )
        tested.execute(connection)

        verify { connection.connect() }

        // failing with exception because nothing was emitted by
        // the underlying channel flow
    }

    @Test
    fun `when connected, subscribe to messages and finish success`() = runTest {
        every { connection.connectionFlow } returns flowOf(
            BillingConnectionState.Loading,
            BillingConnectionState.Connected,
        )
        val result = tested.execute(connection)
        assert(result.isSuccess)
    }

    @Test
    fun `when disconnected, fail`() = runTest {
        val error = Error("expected")
        every { connection.connectionFlow } returns flowOf(
            BillingConnectionState.Loading,
            BillingConnectionState.Disconnected(error),
        )

        val result = tested.execute(connection)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun `only first connection flow value emitted, be that Connected or Disconnected`() = runTest {
        val error = Error("expected")
        every { connection.connectionFlow } returns flowOf(
            // expected
            BillingConnectionState.Connected,

            // not expected to be emitted by the the underlying channel
            // flow because it is closed by now
            BillingConnectionState.Disconnected(error),
        )

        val result = tested.execute(connection)
        assertNotEquals(error, result.exceptionOrNull())
        assert(result.isSuccess)
    }
}
