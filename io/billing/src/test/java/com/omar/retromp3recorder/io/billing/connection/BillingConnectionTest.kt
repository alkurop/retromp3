package com.omar.retromp3recorder.io.billing.connection

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ConnectionState.*
import com.omar.retromp3recorder.domain.toResult
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BillingConnectionTest {

    private val activity = mockk<Activity>(relaxed = true)
    private val scopeJobWrapper = ScopeJobWrapper(UnconfinedTestDispatcher())
    private val connectUC = mockk<ConnectUC>()
    private val billingConnectionListener = mockk<BillingConnectionListener>(relaxed = true)
    private val billingClientProvider = mockk<BillingClientProvider>()
    private val billingClient = mockk<BillingClient>(relaxed = true)
    private lateinit var tested: BillingConnection

    @Before
    fun setup() {
        val billingClientBuilder = mockk<BillingClient.Builder>()
        every { billingClientProvider.provideNewClientBuilder() } returns billingClientBuilder
        every { billingClientBuilder.setListener(any()) } returns billingClientBuilder
        every { billingClientBuilder.build() } returns billingClient
        tested = BillingConnection(
            activity,
            scopeJobWrapper,
            connectUC,
            billingClientProvider,
            billingConnectionListener
        )
    }

    @Test
    fun `WHEN client not ready THEN execute with connection waits for ConnectUC, then action executed`() =
        runTest {
            val successResult = (0)
            every { billingClient.isReady } returns false

            coEvery { connectUC.execute(any()) } returns Result.success(Unit)
            val result = tested.executeWithConnection { successResult.toResult() }.getOrNull()
            coVerify { connectUC.execute(any()) }
            assertEquals(successResult, result)
        }


    @Test
    fun `WHEN client not ready and ConnectUC failed THEN action not executed and failed response`() =
        runTest {
            val connectionError = Error("expected connection")
            val actionError = Error("unexpected")

            every { billingClient.isReady } returns false
            coEvery { connectUC.execute(any()) } returns connectionError.toResult()

            val result = tested.executeWithConnection { Result.failure<Int>(actionError) }
                .exceptionOrNull()

            assertEquals(result, connectionError)
        }

    @Test
    fun `WHEN client ready THEN action executed without attempt to reconnect`() = runTest {
        val actionError = Error("expected action error")
        val connectionError = Error("unexpected")

        every { billingClient.isReady } returns true
        coEvery { connectUC.execute(any()) } returns connectionError.toResult()


        val result = tested.executeWithConnection { Result.failure<Int>(actionError) }
            .exceptionOrNull()

        coVerify(exactly = 0) { connectUC.execute(any()) }
        assertEquals(result, actionError)
    }

    @Test
    fun `WHEN client is connected THEN connection attempt not executed`() = runTest {
        every { billingClient.connectionState } returns CONNECTED
        tested.connect()

        verify(exactly = 0) { billingClient.startConnection(any()) }
        verify(exactly = 0) { billingConnectionListener.setLoading() }
        verify(exactly = 1) { billingClientProvider.provideNewClientBuilder() }
    }

    @Test
    fun `WHEN client is connecting THEN connection attempt not executed`() = runTest {
        every { billingClient.connectionState } returns CONNECTING
        tested.connect()

        verify(exactly = 0) { billingClient.startConnection(any()) }
        verify(exactly = 0) { billingConnectionListener.setLoading() }
        verify(exactly = 1) { billingClientProvider.provideNewClientBuilder() }
    }

    @Test
    fun `WHEN client is disconnected THEN connection attempt executed and listener state updated`() =
        runTest {
            every { billingClient.connectionState } returns DISCONNECTED
            tested.connect()

            verify(exactly = 1) { billingClient.startConnection(any()) }
            verify(exactly = 1) { billingConnectionListener.setLoading() }
            verify(exactly = 1) { billingClientProvider.provideNewClientBuilder() }
        }

    @Test
    fun `WHEN client is closed THEN client recreated connection attempt executed and listener state updated`() =
        runTest {
            every { billingClient.connectionState } returns CLOSED
            tested.connect()

            verify(exactly = 1) { billingClient.startConnection(any()) }
            verify(exactly = 1) { billingConnectionListener.setLoading() }
            verify(exactly = 2) { billingClientProvider.provideNewClientBuilder() }
        }

    @Test
    fun `WHEN disconnect THEN listener updated and client end connection`() = runTest {
        tested.disconnect()
        verify(exactly = 1) { billingClient.endConnection() }
        verify(exactly = 1) { billingConnectionListener.setDisconnected() }
    }
}
