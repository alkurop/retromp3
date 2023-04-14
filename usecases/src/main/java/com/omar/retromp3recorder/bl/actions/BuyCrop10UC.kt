package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.billing.BillingBinderUC
import com.omar.retromp3recorder.bl.billing.count.IncreaseProductCount
import com.omar.retromp3recorder.domain.BillingRequest
import com.omar.retromp3recorder.domain.BillingResponse
import com.omar.retromp3recorder.io.billing.isUserCanceled
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class BuyCrop10UC @Inject constructor(
    private val billingBinderUC: BillingBinderUC,
    private val increaseProductCount: IncreaseProductCount,
    private val audioCoroutineContext: AudioCoroutineContext,
) {
    fun execute() {
        audioCoroutineContext.launch {
            val result = billingBinderUC.execute<BillingResponse.CropProductBuyResponse>(
                BillingRequest.CropProductBuyRequest
            )
            if (result.isFailure) {
                val error = result.exceptionOrNull()!!
                if (error.isUserCanceled()) {
                    Timber.d("BILLING Crop purchase buy canceled")
                } else {
                    Timber.d("BILLING Crop purchase buy error")
                    Timber.e(error)
                }
            } else {
                val purchase = result.getOrThrow().result
                Timber.d("BILLING Crop purchase success $purchase")
                increaseProductCount.execute(
                    purchase.productId,
                    purchase.quantity
                )
            }
        }
    }
}
