package com.omar.retromp3recorder.io.billing.mapping

import com.android.billingclient.api.*
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.BillingError

internal object ResultMapper {

    fun ProductDetailsResult.toProductListResult(): Result<List<ProductDetails>> {
        val productList = productDetailsList
        return billingResult.ifNotFailedResult {
            if (productList.isNullOrEmpty()) {
                Result.failure(BillingError.ConsoleError("Result list null or empty - problems with Play console config"))
            } else {
                Result.success(productList)
            }
        }
    }

    fun PurchasesResult.toPurchasesListResult(): Result<List<PurchaseData>> {
        return billingResult.ifNotFailed { purchasesList.mapNotNull { it.toDomainModel() } }
    }

    fun BillingResult.toResult(): Result<Unit> = ifNotFailed { }

    private fun <T> BillingResult.ifNotFailed(action: () -> T): Result<T> {
        return when (responseCode) {
            BillingClient.BillingResponseCode.OK -> Result.success(action())
            else -> {
                Result.failure(BillingError.OtherError(debugMessage))
            }
        }
    }

    private fun <T> BillingResult.ifNotFailedResult(action: () -> Result<T>): Result<T> {
        return when (responseCode) {
            BillingClient.BillingResponseCode.OK -> action()
            else -> {
                Result.failure(BillingError.OtherError(debugMessage))
            }
        }
    }
}
