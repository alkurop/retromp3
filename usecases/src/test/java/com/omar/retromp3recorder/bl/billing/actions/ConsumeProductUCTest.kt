package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.data.mock.MockProductData
import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.utils.domain.toResult
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
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
class ConsumeProductUCTest {
    private val billing = mockk<Billing>()
    private val listUC = mockk<ListPurchasesUC>()
    private val audioCoroutineContext = AudioCoroutineContext(UnconfinedTestDispatcher())

    private lateinit var tested: ConsumeProductUC

    @Before
    fun setup() {
        tested = ConsumeProductUC(billing, listUC, audioCoroutineContext)
    }

    @Test
    fun `when found product in list then consume`() = runTest {
        val item = MockProductData.giveMockProduct()
        coEvery { listUC.execute() } returns listOf(item).toResult()
        coEvery { billing.postConsumePurchase(any()) } returns Unit.toResult()

        tested.execute(ProductId.CROP_10)

        coVerify { billing.postConsumePurchase(item) }
    }

    @Test
    fun `when found not found product in list then fail`() = runTest {
        val item = MockProductData.giveMockProduct()
        coEvery { listUC.execute() } returns emptyList<PurchaseData>().toResult()
        coEvery { billing.postConsumePurchase(any()) } returns Unit.toResult()

        tested.execute(ProductId.CROP_10)

        coVerify(exactly = 0) { billing.postConsumePurchase(item) }
    }
}
