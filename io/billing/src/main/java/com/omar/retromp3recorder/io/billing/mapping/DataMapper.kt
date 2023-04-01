package com.omar.retromp3recorder.io.billing.mapping

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.omar.retromp3recorder.domain.PayedProducts
import com.omar.retromp3recorder.domain.ProductData

internal object DataMapper {
    fun ProductDetails.toDomainProduct(): ProductData? {
        val id = PayedProducts.values().firstOrNull { this.productId == it.id } ?: return null
        return ProductData(
            id = id,
            name = this.name,
            type = this.productType,
            title = this.title,
            description = this.description,
        )
    }

    fun Purchase.toDomainPurchase(): com.omar.retromp3recorder.domain.PurchaseData? {
        val id = PayedProducts.values().firstOrNull { this.products[0] == it.id } ?: return null
        return com.omar.retromp3recorder.domain.PurchaseData(
            id = id,
            quantity = this.quantity,
            token = this.purchaseToken
        )
    }
}
