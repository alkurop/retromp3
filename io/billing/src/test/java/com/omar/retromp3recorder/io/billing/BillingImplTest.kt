package com.omar.retromp3recorder.io.billing

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.utils.domain.toResult
import com.omar.retromp3recorder.io.billing.connection.BillingConnection
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BillingImplTest {

    private val connection = mockk<BillingConnection>()
    private val audioCoroutineContext = AudioCoroutineContext(UnconfinedTestDispatcher())
    private val activity = mockk<Activity>(relaxed = true)

    private lateinit var tested: BillingImpl

    @Before
    fun setup() {
        tested = BillingImpl(connection, audioCoroutineContext, activity)
    }

    @Test
    fun `WHEN get available products THEN added to cache on success`() = runTest {
        val functionMock = slot<suspend BillingClient.() -> Result<List<ProductDetails>>>()
        val product = mockk<ProductDetails>()
        every { product.productId } returns ProductId.CROP_10.productId

        // #1 add product to cache
        val response = listOf(product).toResult()
        coEvery { connection.executeWithConnection(capture(functionMock)) } returns response

        tested.getAvailableProducts(ProductId.values().toList())

        // #2. Now product is in cache
        coEvery { connection.executeWithConnection(capture(functionMock)) } returns emptyList<ProductDetails>().toResult()

        val result = tested.getAvailableProducts(ProductId.values().toList()).getOrNull()
        val expected = listOf(ProductId.CROP_10)
        assertEquals(result, expected)
    }

    @Test
    fun `WHEN product in cache THEN ui billing flow THEN starts`() = runTest {
        val functionMock = slot<suspend BillingClient.() -> Result<List<ProductDetails>>>()
        val functionMock2 = slot<suspend BillingClient.() -> Result<PurchaseData>>()

        val product = mockk<ProductDetails>()
        every { product.productId } returns ProductId.CROP_10.productId

        val purchase = PurchaseData(
            productId = ProductId.CROP_10,
            isAcknowledged = false,
            purchaseToken = "token",
            quantity = 1
        )

        // #1 Add product do cache
        val response = listOf(product)
        coEvery { connection.executeWithConnection(capture(functionMock)) } returns response.toResult()
        tested.getAvailableProducts(ProductId.values().toList())

        //#2 start billing ui flow
        coEvery { connection.executeWithConnection(capture(functionMock2)) } returns purchase.toResult()
        val result = tested.uiLaunchBillingFlow(ProductId.CROP_10)
        assertEquals(result.getOrNull(), purchase)
    }


    @Test
    fun `WHEN product with id not found THEN ui billing flow fails`() = runTest {
        val functionMock2 = slot<suspend BillingClient.() -> Result<PurchaseData>>()

        val product = mockk<ProductDetails>()
        every { product.productId } returns ProductId.CROP_10.productId

        val purchase = PurchaseData(
            productId = ProductId.CROP_10,
            isAcknowledged = false,
            purchaseToken = "token",
            quantity = 1
        )

        //#1 start billing ui flow without cached product, which returns a Failed result with an exception
        coEvery { connection.executeWithConnection(capture(functionMock2)) } returns purchase.toResult()
        val result = tested.uiLaunchBillingFlow(ProductId.CROP_10)
        assertNotNull(result.exceptionOrNull())
    }
}
