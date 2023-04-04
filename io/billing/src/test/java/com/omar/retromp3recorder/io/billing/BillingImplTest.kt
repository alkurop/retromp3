package com.omar.retromp3recorder.io.billing

import android.app.Activity
import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.domain.toResult
import com.omar.retromp3recorder.io.billing.connection.BillingConnection
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BillingImplTest {

    private val connection = mockk<BillingConnection>(relaxed = true)
    private val scopeJobWrapper = ScopeJobWrapper(UnconfinedTestDispatcher())
    private val activity = mockk<Activity>(relaxed = true)

    private lateinit var tested: BillingImpl

    @Before
    fun setup() {
        tested = BillingImpl(connection, scopeJobWrapper, activity)
    }

    @Test
    fun `WHEN get available products THEN added to cache on success`() = runTest {
        // #1
        val result = listOf<ProductData>()
        coEvery { connection.executeWithConnection<List<ProductData>> { any() } } returns emptyList<ProductData>().toResult()

    }

    @Test
    fun `WHEN get available products THEN returned cached products if not empty`() = runTest {

    }


    @Test
    fun `WHEN product with id not found THEN ui billing flow fails`() = runTest { }

    @Test
    fun `WHEN ui billing flow THEN receive next result from connection listener`() = runTest { }

    @Test
    fun `WHEN get active purchases THEN connection updated to cache`() = runTest { }

}
