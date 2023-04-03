package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.ProductId
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
            billing.getAvailableProducts(ProductId.values().map { it.productId })
        }
    }
}
