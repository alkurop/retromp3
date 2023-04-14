package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.utils.domain.chain
import com.omar.retromp3recorder.utils.domain.chainSuspend
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.io.billing.mapping.findPurchase
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ConsumeProductUC @Inject constructor(
    private val billing: Billing,
    private val listUC: ListPurchasesUC,
    private val audioCoroutineContext: AudioCoroutineContext
) {
    suspend fun execute(productId: ProductId): Result<Unit> {
        return withContext(audioCoroutineContext.coroutineContext) {
            Timber.d("BILLING Product trying to consume $productId")
            listUC.execute()
                .chain { it.findPurchase(productId) }
                .chainSuspend { billing.postConsumePurchase(it) }
                .onSuccess {
                    Timber.d("BILLING Product consumed $it")
                }
        }
    }
}
