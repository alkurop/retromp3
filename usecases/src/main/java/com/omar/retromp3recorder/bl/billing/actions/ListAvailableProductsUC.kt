package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.io.billing.Billing
import timber.log.Timber
import javax.inject.Inject

class ListAvailableProductsUC @Inject constructor(
    private val billing: Billing,
) {
    suspend fun execute(): Result<List<ProductId>> {
        Timber.d("BILLING Product trying list")

        return billing.getAvailableProducts(ProductId.values().toList())
            .onSuccess {
                Timber.d("BILLING Product listed $it")
            }
    }
}
