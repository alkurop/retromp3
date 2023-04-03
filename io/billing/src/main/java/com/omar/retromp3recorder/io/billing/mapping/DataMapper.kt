package com.omar.retromp3recorder.io.billing.mapping

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.domain.toResult
import com.omar.retromp3recorder.io.billing.BillingError
import timber.log.Timber

fun ProductDetails.toDomainModel(): ProductData? {
    val id = ProductId.values().firstOrNull { this.productId == it.productId } ?: return null
    return ProductData(
        productType = id,
        name = this.name,
        title = this.title,
        description = this.description,
    )
}

fun Purchase.toDomainModel(): PurchaseData? {
    val productId = ProductId.values().firstOrNull { this.products[0] == it.productId }
    if (productId == null) {
        Timber.e("Product id not found it purchase list ${this.products}")
        return null
    } else if (this.products.size > 1) {
        Timber.e("Product id list is more then one in purchase ${this.products}")
    }
    this.packageName
    return PurchaseData(
        productId = productId,
        quantity = this.quantity,
        token = this.purchaseToken,
        isAcknowledged = this.isAcknowledged,
        orderId = this.orderId,
        purchaseToken = this.purchaseToken,
        timestamp = this.purchaseTime,
        signature = this.signature,
        originalJson = this.originalJson
    )
}

fun List<ProductData>.findProduct(productType: ProductId): Result<ProductData> {
    val cropProduct = this.firstOrNull { item -> item.productType == productType }
    return cropProduct?.toResult() ?: BillingError.OtherError("Product not found $productType")
        .toResult()
}

fun List<PurchaseData>.findPurchase(productType: ProductId): Result<PurchaseData> {
    val product = this.firstOrNull { item ->
        item.productId == productType
    }
    return product?.toResult() ?: BillingError.OtherError("Product not found $productType")
        .toResult()
}

