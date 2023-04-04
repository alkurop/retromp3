package com.omar.retromp3recorder.domain


enum class ProductId(val productId: String) {
    CROP_10("crop_times_10")
}

data class PurchaseData(
    val productId: ProductId,
    val token: String,
    val quantity: Int,
    val isAcknowledged: Boolean,
    val orderId: String,
    val purchaseToken: String,
    val timestamp: Long,
    val signature: String,
    val originalJson: String
)
