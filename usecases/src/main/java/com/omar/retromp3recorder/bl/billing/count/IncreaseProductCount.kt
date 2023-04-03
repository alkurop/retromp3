package com.omar.retromp3recorder.bl.billing.count

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.storage.repo.global.CropProductRepo
import timber.log.Timber
import javax.inject.Inject

class IncreaseProductCount @Inject constructor(
    private val productRepo: CropProductRepo
) {
    suspend fun execute(productId: ProductId, addedCount: Int) {
        when (productId) {
            ProductId.CROP_10 -> {
                val available = productRepo.first()
                val newCount = available + addedCount
                Timber.d("BILLING Product count increased to $newCount")
                productRepo.emit(newCount)
            }
        }
    }
}
