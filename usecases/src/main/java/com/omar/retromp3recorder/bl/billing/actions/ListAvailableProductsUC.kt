package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ListAvailableProductsUC @Inject constructor(
    private val billing: Billing,
    private val audioCoroutineContext: AudioCoroutineContext,
) {
    suspend fun execute(): Result<List<ProductId>> {
        return withContext(audioCoroutineContext.coroutineContext) {
            Timber.d("BILLING Product trying list")

            billing.getAvailableProducts(ProductId.values().toList())
                .onSuccess {
                    Timber.d("BILLING Product listed $it")
                }
        }
    }
}
