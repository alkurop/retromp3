package com.omar.retromp3recorder.domain


enum class ProductId(val productId: String, val quantityMultiplier: Int = 1) {
    CROP_10("crop_times_10",10)
}

data class PurchaseData(
    val productId: ProductId,
    val purchaseToken: String,
    val quantity: Int,
    val isAcknowledged: Boolean,
)
