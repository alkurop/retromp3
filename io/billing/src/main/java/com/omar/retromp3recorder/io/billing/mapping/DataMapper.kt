package com.omar.retromp3recorder.io.billing.mapping

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.domain.toResult
import com.omar.retromp3recorder.io.billing.BillingError
import timber.log.Timber

fun ProductDetails.toDomainModel(): ProductId? =
    ProductId.values().firstOrNull { this.productId == it.productId }

fun Purchase.toDomainModel(): PurchaseData? {
    val productId = ProductId.values().firstOrNull { this.products[0] == it.productId }
    if (productId == null) {
        Timber.e("BILLING Product id not found it purchase list ${this.products}")
        return null
    } else if (this.products.size > 1) {
        Timber.e("BILLING Product id list is more then one in purchase ${this.products}")
    }
    this.packageName
    return PurchaseData(
        productId = productId,
        quantity = this.quantity,
        purchaseToken = this.purchaseToken,
        isAcknowledged = this.isAcknowledged,
    )
}

fun List<ProductId>.findProduct(productType: ProductId): Result<ProductId> =
    if (this.contains(productType)) productType.toResult() else BillingError.OtherError("Product not found $productType")
        .toResult()

fun List<PurchaseData>.findPurchase(productType: ProductId): Result<PurchaseData> =
    this.firstOrNull { item ->
        item.productId == productType
    }?.toResult() ?: BillingError.OtherError("Product not found $productType")
        .toResult()
