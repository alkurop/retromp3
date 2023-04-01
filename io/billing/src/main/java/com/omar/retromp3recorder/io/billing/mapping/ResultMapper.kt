package com.omar.retromp3recorder.io.billing.mapping

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.PurchasesResult
import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.BillingError
import com.omar.retromp3recorder.io.billing.mapping.DataMapper.toDomainProduct
import com.omar.retromp3recorder.io.billing.mapping.DataMapper.toDomainPurchase

internal object ResultMapper {
    fun ProductDetailsResult.toProductListResult(): Result<List<ProductData>> {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        val productList = productDetailsList

        return when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (productList.isNullOrEmpty()) {
                    Result.failure(BillingError.ConsoleError("Result list null or empty - problems with Play console config"))
                } else {
                    Result.success(productList.mapNotNull { it.toDomainProduct() })
                }
            }
            else -> {
                Result.failure(BillingError.OtherError(debugMessage))
            }
        }
    }

    fun PurchasesResult.toPurchasesListResult(): Result<List<PurchaseData>> {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        val purchaseList = this.purchasesList

        return when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                Result.success(purchaseList.mapNotNull { it.toDomainPurchase() })
            }
            else -> {
                Result.failure(BillingError.OtherError(debugMessage))
            }
        }
    }

}
