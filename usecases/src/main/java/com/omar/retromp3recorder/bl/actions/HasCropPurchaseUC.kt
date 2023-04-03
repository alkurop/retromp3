package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.billing.BillingBinderUC
import com.omar.retromp3recorder.bl.billing.count.IncreaseProductCount
import com.omar.retromp3recorder.domain.BillingRequest
import com.omar.retromp3recorder.domain.BillingResponse
import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.io.billing.isUserCanceled
import com.omar.retromp3recorder.storage.repo.global.CropProductRepo
import com.omar.retromp3recorder.utils.domain.LoadingState
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HasCropPurchaseUC @Inject constructor(
    private val cropProductRepo: CropProductRepo,
    private val billingBinderUC: BillingBinderUC,
    private val increaseProductCount: IncreaseProductCount,
    private val scopeJobWrapper: ScopeJobWrapper,
) {
    fun execute(): Flow<LoadingState<Boolean>> = flow {
        val cropProductCount = cropProductRepo.first()
        if (cropProductCount > 0) {
            emit(LoadingState.Success(true))
        } else {
            emit(LoadingState.Loading())
            withContext(scopeJobWrapper.coroutineContext) {
                val result = billingBinderUC.execute<BillingResponse.CropProductBuyResponse>(
                    BillingRequest.CropProductConsumeRequest
                )
                if (result.isFailure) {
                    val error = result.exceptionOrNull()!!
                    if (error.isUserCanceled()) {
                        emit(LoadingState.Success(false))
                    } else {
                        emit(LoadingState.Failed(error))
                    }
                } else {
                    val purchase = result.getOrThrow().result
                    increaseProductCount.execute(ProductId.CROP_10, purchase.quantity * 10)
                    emit(LoadingState.Success(true))
                }
            }
        }
    }
}
