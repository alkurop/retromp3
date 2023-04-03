package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.domain.chain
import com.omar.retromp3recorder.domain.chainSuspend
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.io.billing.mapping.findPurchase
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConsumeProductUC @Inject constructor(
    private val billing: Billing,
    private val listUC: ListPurchasesUC,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(productId: ProductId): Result<Unit> {
        return withContext(scopeJobWrapper.coroutineContext) {
            listUC.execute()
                .chain { it.findPurchase(productId) }
                .chainSuspend { billing.postConsumePurchase(it) }
        }
    }
}
