package com.omar.retromp3recorder.bl.billing.count

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.storage.repo.global.CropProductRepo
import timber.log.Timber
import javax.inject.Inject

class ShouldConsumeProductUC @Inject constructor(
    private val cropProductRepo: CropProductRepo
) {
    suspend fun execute(productId: ProductId): Boolean {
        when (productId) {
            ProductId.CROP_10 -> {
                val available = cropProductRepo.first()
                val shouldConsume = available <= 0 && cropProductRepo.isTrial().not()
                Timber.d("BILLING Product purchase should consume $shouldConsume")
                return shouldConsume
            }
        }
    }
}
