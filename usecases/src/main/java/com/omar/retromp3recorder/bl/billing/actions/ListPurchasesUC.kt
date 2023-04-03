package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListPurchasesUC @Inject constructor(
    private val billing: Billing,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(): Result<List<PurchaseData>> {
        return withContext(scopeJobWrapper.coroutineContext) {
            billing.getUserActivePurchases()
        }
    }
}
