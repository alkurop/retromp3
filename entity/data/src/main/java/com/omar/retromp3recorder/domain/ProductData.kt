package com.omar.retromp3recorder.domain

data class ProductData(
    val name: String,
    val description: String,
    val productType: PayedProducts,
    val type: String,
    val title: String
)


enum class PayedProducts(val productId: String) {
    CROP_10("crop_times_10")
}


data class PurchaseData(
    val id: PayedProducts,
    val token: String,
    val quantity: Int
)
