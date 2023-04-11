package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.data.mock.MockProductData
import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.utils.domain.toResult
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BuyProductUCTest {
    private val billing = mockk<Billing>()
    private val listProductUC = mockk<ListAvailableProductsUC>()
    private val listPurchasesUC = mockk<ListPurchasesUC>()
    private val scopeJobWrapper = ScopeJobWrapper(UnconfinedTestDispatcher())
    private lateinit var tested: BuyProductUC

    @Before
    fun setup() {
        tested = BuyProductUC(billing, listProductUC, listPurchasesUC, scopeJobWrapper)
    }

    @Test
    fun `when has unconsumed purchase skip purchase`() = runTest {
        val unconsumedProduct = MockProductData.giveMockProduct(isAcknowledged = true)
        coEvery { listPurchasesUC.execute() } returns listOf(unconsumedProduct).toResult()
        val result = tested.execute(ProductId.CROP_10)

        coVerify(exactly = 1) { listPurchasesUC.execute() }
        coVerify(exactly = 0) { listProductUC.execute() }
        coVerify(exactly = 0) { billing.postAcknowledgePurchase(unconsumedProduct) }
        coVerify(exactly = 0) { billing.uiLaunchBillingFlow(any()) }

        assertEquals(unconsumedProduct, result.getOrNull())
    }

    @Test
    fun `when unconsumed purchase not acknowledged then acknowledge`() = runTest {
        val unconsumedProduct = MockProductData.giveMockProduct(isAcknowledged = false)
        coEvery { listPurchasesUC.execute() } returns listOf(unconsumedProduct).toResult()
        coEvery { billing.postAcknowledgePurchase(any()) } returns Result.success(Unit)

        val result = tested.execute(ProductId.CROP_10)

        coVerify(exactly = 1) { listPurchasesUC.execute() }
        coVerify(exactly = 0) { listProductUC.execute() }
        coVerify(exactly = 1) { billing.postAcknowledgePurchase(unconsumedProduct) }
        coVerify(exactly = 0) { billing.uiLaunchBillingFlow(any()) }

        assertEquals(unconsumedProduct, result.getOrNull())
    }

    @Test
    fun `when no unconsumed purchase, list products, run payment flow and acknowledge`() =
        runTest {
            val newPurchase = MockProductData.giveMockProduct(isAcknowledged = false)

            coEvery { listPurchasesUC.execute() } returns emptyList<PurchaseData>().toResult()
            coEvery { listProductUC.execute() } returns listOf(ProductId.CROP_10).toResult()
            coEvery { billing.postAcknowledgePurchase(any()) } returns Result.success(Unit)
            coEvery { billing.uiLaunchBillingFlow(any()) } returns Result.success(newPurchase)

            val result = tested.execute(ProductId.CROP_10)

            coVerify(exactly = 1) { listPurchasesUC.execute() }
            coVerify(exactly = 1) { listProductUC.execute() }
            coVerify(exactly = 1) { billing.postAcknowledgePurchase(newPurchase) }
            coVerify(exactly = 1) { billing.uiLaunchBillingFlow(any()) }

            assertEquals(newPurchase, result.getOrNull())
        }
}
