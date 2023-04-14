package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.Billing
import timber.log.Timber
import javax.inject.Inject

class ListPurchasesUC @Inject constructor(
    private val billing: Billing,
) {
    suspend fun execute(): Result<List<PurchaseData>> {
        Timber.d("BILLING Purchase trying list")
        return billing.getUserActivePurchases()
            .onSuccess {
                Timber.d("BILLING Purchase listed $it")
            }
    }
}
