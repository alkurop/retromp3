package com.omar.retromp3recorder.bl.billing.count

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.storage.repo.global.CropProductRepo
import timber.log.Timber
import javax.inject.Inject

class ShouldConsumeProductUC @Inject constructor(
    private val productRepo: CropProductRepo
) {
    suspend fun execute(productId: ProductId): Boolean {
        when (productId) {
            ProductId.CROP_10 -> {
                val available = productRepo.first()
                val shouldConsume = available <= 0
                Timber.d("BILLING Product purchase should consume $shouldConsume")
                return shouldConsume
            }
        }
    }
}
