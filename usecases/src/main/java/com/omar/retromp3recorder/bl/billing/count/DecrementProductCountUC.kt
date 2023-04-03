package com.omar.retromp3recorder.bl.billing.count

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.storage.repo.global.CropProductRepo
import javax.inject.Inject

class DecrementProductCountUC @Inject constructor(
    private val productRepo: CropProductRepo
) {
    suspend fun execute(productId: ProductId): Boolean {
        when (productId) {
            ProductId.CROP_10 -> {
                val available = productRepo.first()
                return if (available > 0) {
                    productRepo.emit(available - 1)
                    true
                } else {
                    false
                }
            }
        }
    }
}
