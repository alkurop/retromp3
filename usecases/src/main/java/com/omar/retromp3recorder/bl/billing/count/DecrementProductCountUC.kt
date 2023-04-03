package com.omar.retromp3recorder.bl.billing.count

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.storage.repo.global.CropProductRepo
import timber.log.Timber
import javax.inject.Inject

class DecrementProductCountUC @Inject constructor(
    private val productRepo: CropProductRepo
) {
    suspend fun execute(productId: ProductId): Boolean {
        Timber.d("BILLING Purchase trying to decrement $productId")

        when (productId) {
            ProductId.CROP_10 -> {
                val available = productRepo.first()
                return if (available > 0) {
                    val newCount = available - 1
                    Timber.d("BILLING Product count decremented to $newCount")
                    productRepo.emit(newCount)
                    true
                } else {
                    false
                }
            }
        }
    }
}
