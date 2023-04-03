package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.billing.BillingBinderUC
import com.omar.retromp3recorder.bl.billing.count.DecrementProductCountUC
import com.omar.retromp3recorder.bl.billing.count.ShouldConsumeProductUC
import com.omar.retromp3recorder.domain.*
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CropWithProductUC @Inject constructor(
    private val cropUC: CropUC,
    private val billingBinderUC: BillingBinderUC,
    private val decrementProductCountUC: DecrementProductCountUC,
    private val shouldConsumeProductUC: ShouldConsumeProductUC,
    private val jobWrapper: ScopeJobWrapper,

    ) {
    suspend fun execute(nameSuggestion: NewNameSuggestion): Result<ExistingFileWrapper> {
        val cropResult = cropUC.execute(nameSuggestion)
        if (cropResult.isSuccess) {
            val wasDecremented = decrementProductCountUC.execute(ProductId.CROP_10)
            if (wasDecremented && shouldConsumeProductUC.execute(ProductId.CROP_10)) {
                jobWrapper.launch {
                    billingBinderUC.execute<BillingResponse.CropProductConsumeResponse>(
                        BillingRequest.CropProductConsumeRequest
                    ).also {
                        it.onFailure { error ->
                            Timber.e(error)
                        }
                    }
                }
            }
        }
        return cropResult
    }
}
