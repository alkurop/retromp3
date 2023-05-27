package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.billing.BillingBinderUC
import com.omar.retromp3recorder.bl.billing.count.DecrementProductCountUC
import com.omar.retromp3recorder.bl.billing.count.ShouldConsumeProductUC
import com.omar.retromp3recorder.domain.BillingResponse
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.io.billing.di.BillingCoroutineScope
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
class CropWithProductUCTest {
    private val cropUC = mockk<CropUC>()
    private val billingBinderUC = mockk<BillingBinderUC>()
    private val decrementProductCountUC = mockk<DecrementProductCountUC>()
    private val shouldConsumeProductUC = mockk<ShouldConsumeProductUC>()

    private lateinit var tested: CropWithProductUC
    private val billingCoroutineScope= BillingCoroutineScope(UnconfinedTestDispatcher())

    @Before
    fun setup() {
        tested = CropWithProductUC(
            cropUC,
            billingBinderUC,
            decrementProductCountUC,
            shouldConsumeProductUC,
            billingCoroutineScope
        )
    }

    @Test
    fun `when crop failed do nothing`() = runTest {
        val error = Error("expected")
        coEvery { cropUC.execute(any()) } returns error.toResult()

        tested.execute(mockk())

        coVerify(exactly = 0) { decrementProductCountUC.execute(any()) }
        coVerify(exactly = 0) { shouldConsumeProductUC.execute(any()) }
        coVerify(exactly = 0) {
            billingBinderUC.execute<BillingResponse.CropProductConsumeResponse>(any())
        }
    }

    @Test
    fun `when crop success and was not decrement, do nothing`() = runTest {
        coEvery { cropUC.execute(any()) } returns mockk<ExistingFileWrapper>().toResult()
        coEvery { decrementProductCountUC.execute(any()) } returns false

        tested.execute(mockk())

        coVerify(exactly = 1) { decrementProductCountUC.execute(any()) }
        coVerify(exactly = 0) { shouldConsumeProductUC.execute(any()) }
        coVerify(exactly = 0) {
            billingBinderUC.execute<BillingResponse.CropProductConsumeResponse>(any())
        }
    }

    @Test
    fun `when crop success and was decremented but should not consumed, do nothing`() = runTest {
        coEvery { cropUC.execute(any()) } returns mockk<ExistingFileWrapper>().toResult()
        coEvery { decrementProductCountUC.execute(any()) } returns true
        coEvery { shouldConsumeProductUC.execute(any()) } returns false


        tested.execute(mockk())

        coVerify(exactly = 1) { decrementProductCountUC.execute(any()) }
        coVerify(exactly = 1) { shouldConsumeProductUC.execute(any()) }
        coVerify(exactly = 0) {
            billingBinderUC.execute<BillingResponse.CropProductConsumeResponse>(any())
        }
    }

    @Test
    fun `when crop success and was decremented should consume, then consume`() = runTest {
        coEvery { cropUC.execute(any()) } returns mockk<ExistingFileWrapper>().toResult()
        coEvery { decrementProductCountUC.execute(any()) } returns true
        coEvery { shouldConsumeProductUC.execute(any()) } returns true
        coEvery {
            billingBinderUC
                .execute<BillingResponse.CropProductConsumeResponse>(any())
        } returns mockk<BillingResponse.CropProductConsumeResponse>().toResult()


        tested.execute(mockk())

        coVerify(exactly = 1) { decrementProductCountUC.execute(any()) }
        coVerify(exactly = 1) { shouldConsumeProductUC.execute(any()) }
        coVerify(exactly = 1) {
            billingBinderUC.execute<BillingResponse.CropProductConsumeResponse>(any())
        }
    }

}
