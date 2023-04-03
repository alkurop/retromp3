package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.billing.BillingBinderUC
import com.omar.retromp3recorder.bl.billing.count.IncreaseProductCount
import com.omar.retromp3recorder.domain.BillingRequest
import com.omar.retromp3recorder.domain.BillingResponse
import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.io.billing.isUserCanceled
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class BuyCropFlowUC @Inject constructor(
    private val billingBinderUC: BillingBinderUC,
    private val increaseProductCount: IncreaseProductCount,
    private val scopeJobWrapper: ScopeJobWrapper,
) {
    fun execute() {
        scopeJobWrapper.launch {
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
                    increaseProductCount.execute(ProductId.CROP_10, 1)
                }
            } else {
                val purchase = result.getOrThrow().result
                Timber.d("BILLING Crop purchase success $purchase")
                increaseProductCount.execute(ProductId.CROP_10, purchase.quantity * 10)
            }
        }
    }
}
