package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ListPurchasesUC @Inject constructor(
    private val billing: Billing,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(): Result<List<PurchaseData>> {
        Timber.d("BILLING Purchase trying list")
        return withContext(scopeJobWrapper.coroutineContext) {
            billing.getUserActivePurchases()
                .onSuccess {
                    Timber.d("BILLING Purchase listed $it")
                }
        }
    }
}
