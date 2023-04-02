package com.omar.retromp3recorder.bl.billing

import com.omar.retromp3recorder.domain.PayedProducts
import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListAvailableProductsUC @Inject constructor(
    private val billing: Billing,
    private val scopeJobWrapper: ScopeJobWrapper,
) {
    suspend fun execute(): Result<List<ProductData>> {
        return withContext(scopeJobWrapper.coroutineContext) {
            billing.getProductDetails(PayedProducts.values().map { it.productId })
        }
    }
}
