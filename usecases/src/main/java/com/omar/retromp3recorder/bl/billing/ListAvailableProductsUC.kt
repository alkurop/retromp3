package com.omar.retromp3recorder.bl.billing

import com.omar.retromp3recorder.domain.PayedProducts
import com.omar.retromp3recorder.domain.Product
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListAvailableProductsUC @Inject constructor(
    private val connectBillingUC: ConnectToBillingUC,
    private val billing: Billing,
    private val scopeJobWrapper: ScopeJobWrapper,
) {
    suspend fun execute(): Result<List<Product>> {
        return withContext(scopeJobWrapper.coroutineContext) {
            val connectionResult = connectBillingUC.execute()
            if (connectionResult.isSuccess) {
                billing.queryProductDetails(PayedProducts.values().map { it.id })
            } else {
                Result.failure(connectionResult.exceptionOrNull()!!)
            }
        }
    }
}
