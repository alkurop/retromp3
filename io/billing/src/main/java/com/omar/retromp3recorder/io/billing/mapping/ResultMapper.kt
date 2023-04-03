package com.omar.retromp3recorder.io.billing.mapping

import com.android.billingclient.api.*
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.domain.toResult
import com.omar.retromp3recorder.io.billing.BillingError
import com.omar.retromp3recorder.io.billing.connection.PurchaseUpdateData

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

    fun PurchasesResult.toPurchasesListResult(): Result<List<Purchase>> =
        billingResult.ifNotFailed { purchasesList }

    fun BillingResult.toResult(): Result<Unit> = ifNotFailed { }

    fun <T> BillingResult.ifNotFailed(action: () -> T): Result<T> {
        return this.ifNotFailedResult { action().toResult() }
    }

    private fun <T> BillingResult.ifNotFailedResult(action: () -> Result<T>): Result<T> {
        return when (responseCode) {
            BillingClient.BillingResponseCode.OK -> action()
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                BillingError.UserCanceled.toResult()
            }
            else -> {
                BillingError.OtherError(debugMessage).toResult()
            }
        }
    }

    fun PurchaseUpdateData.toResult(): Result<PurchaseData> {
        val delta = this.addedItems
        val error = this.error
        return error?.toResult()
            ?: if (delta.size == 1) {
                delta[0].toDomainModel()?.toResult()
                    ?: BillingError.OtherError("Purchase not recognized")
                        .toResult()
            } else if (delta.size > 1) {
                BillingError.OtherError("Purchase update does not contain ${delta.size} purchases")
                    .toResult()
            } else {
                BillingError.OtherError("Purchase update does not contain a new purchase")
                    .toResult()
            }
    }
}
