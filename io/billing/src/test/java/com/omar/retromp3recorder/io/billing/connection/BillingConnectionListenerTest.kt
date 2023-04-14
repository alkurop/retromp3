package com.omar.retromp3recorder.io.billing.connection

import app.cash.turbine.test
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BillingConnectionListenerTest {
    private val jobWrapper = AudioCoroutineContext(UnconfinedTestDispatcher())
    private lateinit var tested: BillingConnectionListener
    private val result = mockk<BillingResult>()

    @Before
    fun setup() {
        every { result.debugMessage } returns "hello"
        tested = BillingConnectionListener(jobWrapper)
    }

    @Test
    fun `on loading triggered, connection state updated`() = runTest {
        tested.setLoading()
        assertEquals(BillingConnectionState.Loading, tested.connectionState.value)
    }

    @Test
    fun `on disconnected triggered, connection state updated`() = runTest {
        tested.setDisconnected()

        val connectionState = tested.connectionState.value
        assert(connectionState is BillingConnectionState.Disconnected)
    }

    @Test
    fun `when setup finished OK then connection state CONNECTED`() = runTest {
        every { result.responseCode } returns BillingClient.BillingResponseCode.OK

        tested.onBillingSetupFinished(result)

        val connectionState = tested.connectionState.value
        assert(connectionState is BillingConnectionState.Connected)
    }

    @Test
    fun `when setup finished FAILED then connection state DISCONNECTED`() = runTest {
        every { result.responseCode } returns BillingClient.BillingResponseCode.USER_CANCELED

        tested.onBillingSetupFinished(result)

        val connectionState = tested.connectionState.value
        assert(connectionState is BillingConnectionState.Disconnected)
    }

    @Test
    fun `when purchase update with OK result, cache updated with received list`() = runTest {
        every { result.responseCode } returns BillingClient.BillingResponseCode.OK

        val purchase = listOf(mockk<Purchase>())

        tested.purchaseUpdateFlow.test {
            //emit after subscription
            tested.onPurchasesUpdated(result, purchase)
            val response = awaitItem()


            assertEquals(purchase, response.purchaseList)
            assertEquals(purchase, response.addedItems)
            assertNull(response.error)
        }
    }

    @Test
    fun `when purchase update with FAILED result, cache updated with resultError`() = runTest {
        every { result.responseCode } returns BillingClient.BillingResponseCode.ERROR

        val purchase = listOf(mockk<Purchase>())

        tested.purchaseUpdateFlow.test {
            //emit after subscription
            tested.onPurchasesUpdated(result, purchase)
            val response = awaitItem()

            assert( response.purchaseList.isEmpty())
            assert( response.addedItems.isEmpty())
            assertNotNull(response.error)
        }
    }
}
