package com.omar.retromp3recorder.domain

data class Product(
    val name: String,
    val description: String,
    val id: String,
    val type: String,
    val title: String
)


enum class PayedProducts(val id: String) {
    CROP_10("crop_items_10")
}
