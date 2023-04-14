package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.billing.BillingBinderUC
import com.omar.retromp3recorder.bl.billing.count.IncreaseProductCount
import com.omar.retromp3recorder.domain.*
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import com.omar.retromp3recorder.utils.domain.toResult
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
class BuyCrop10UCTest {
    private val billingBinderUC = mockk<BillingBinderUC>()
    private val increaseProductCount = mockk<IncreaseProductCount>()
    private val audioCoroutineContext = AudioCoroutineContext(UnconfinedTestDispatcher())

    private lateinit var tested: BuyCrop10UC

    @Before
    fun setup() {
        tested = BuyCrop10UC(billingBinderUC, increaseProductCount, audioCoroutineContext)
    }

    @Test
    fun `when billingbinder failed result no action`() = runTest {
        coEvery {
            billingBinderUC.execute<BillingResponse.CropProductBuyResponse>(
                BillingRequest.CropProductBuyRequest
            )
        } returns Result.failure(Error("expected"))

        tested.execute()

        coVerify(exactly = 0) { increaseProductCount.execute(any(), any()) }
    }

    @Test
    fun `when billingbinder success, product count increased`() = runTest {
        val result = PurchaseData(
            productId = ProductId.CROP_10,
            quantity = 3,
            purchaseToken = "token",
            isAcknowledged = true
        )

        coEvery {
            billingBinderUC.execute<BillingResponse.CropProductBuyResponse>(
                BillingRequest.CropProductBuyRequest
            )
        } returns BillingResponse.CropProductBuyResponse(result).toResult()

        tested.execute()

        coVerify(exactly = 1) {
            increaseProductCount.execute(
                result.productId,
                result.quantity
            )
        }
    }
}
