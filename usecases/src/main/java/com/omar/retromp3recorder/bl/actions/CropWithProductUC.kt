package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.billing.BillingBinderUC
import com.omar.retromp3recorder.bl.billing.count.DecrementProductCountUC
import com.omar.retromp3recorder.bl.billing.count.ShouldConsumeProductUC
import com.omar.retromp3recorder.domain.*
import com.omar.retromp3recorder.io.billing.di.BillingCoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CropWithProductUC @Inject constructor(
    private val cropUC: CropUC,
    private val billingBinderUC: BillingBinderUC,
    private val decrementProductCountUC: DecrementProductCountUC,
    private val shouldConsumeProductUC: ShouldConsumeProductUC,
    private val billingCoroutineScope: BillingCoroutineScope
) {
    suspend fun execute(nameSuggestion: NewNameSuggestion): Result<ExistingFileWrapper> {
        val cropResult = cropUC.execute(nameSuggestion)
        if (cropResult.isSuccess) {
            billingCoroutineScope.launch {
                val wasDecremented = decrementProductCountUC.execute(ProductId.CROP_10)
                if (wasDecremented && shouldConsumeProductUC.execute(ProductId.CROP_10)) {
                    billingBinderUC.execute<BillingResponse.CropProductConsumeResponse>(
                        BillingRequest.CropProductConsumeRequest
                    ).onFailure { error ->
                        Timber.e(error)
                    }
                }
            }
        }
        return cropResult
    }
}
