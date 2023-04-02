package com.omar.retromp3recorder.io.billing.mapping

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.omar.retromp3recorder.domain.PayedProducts
import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.domain.PurchaseData

fun ProductDetails.toDomainModel(): ProductData? {
    val id = PayedProducts.values().firstOrNull { this.productId == it.productId } ?: return null
    return ProductData(
        productType = id,
        name = this.name,
        type = this.productType,
        title = this.title,
        description = this.description,
    )
}

fun Purchase.toDomainModel(): PurchaseData? {
    val id = PayedProducts.values().firstOrNull { this.products[0] == it.productId } ?: return null
    return PurchaseData(
        id = id,
        quantity = this.quantity,
        token = this.purchaseToken
    )
}
