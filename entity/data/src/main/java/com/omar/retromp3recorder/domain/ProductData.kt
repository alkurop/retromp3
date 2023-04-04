package com.omar.retromp3recorder.domain


enum class ProductId(val productId: String) {
    CROP_10("crop_times_10")
}

data class PurchaseData(
    val productId: ProductId,
    val purchaseToken: String,
    val quantity: Int,
    val isAcknowledged: Boolean,
)
