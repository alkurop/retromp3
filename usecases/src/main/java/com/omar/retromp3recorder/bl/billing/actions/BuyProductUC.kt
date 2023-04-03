package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.*
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.io.billing.mapping.findProduct
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BuyProductUC @Inject constructor(
    private val billing: Billing,
    private val listUC: ListAvailableProductsUC,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(productId: ProductId): Result<PurchaseData> {
        return withContext(scopeJobWrapper.coroutineContext) {
            listUC.execute()
                .chain {
                    it.findProduct(productId)
                }
                .chainSuspend { billing.uiLaunchBillingFlow(it) }
                .chainSuspend { purchase ->
                    billing.postAcknowledgePurchase(purchase).map { purchase }
                }
        }
    }
}
