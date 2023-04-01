package com.omar.retromp3recorder.io.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.QueryProductDetailsParams
import com.omar.retromp3recorder.domain.Product

internal object ProductDetailsResultMapper {
    fun ProductDetailsResult.toProductResult(): Result<List<Product>> {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        return when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {

                val productDetailsList1 = productDetailsList
                if (productDetailsList1.isNullOrEmpty()) {
                    Result.failure(BillingError.ConsoleError("Result list null or empty - problems with Play console config"))
                } else {
                    Result.success(productDetailsList1!!.map { it.toDomainProduct() })
                }
            }
            else -> {
                Result.failure(BillingError.OtherError(debugMessage))
            }
        }
    }

    fun List<String>.toProductQueryParams(): QueryProductDetailsParams{
       return QueryProductDetailsParams.newBuilder().setProductList(
            this.map {
                QueryProductDetailsParams.Product.newBuilder().setProductId(it)
                    .setProductType(BillingClient.ProductType.INAPP).build()
            }
        ).build()
    }

    private fun ProductDetails.toDomainProduct() = Product(
        id = this.productId,
        name = this.name,
        type = this.productType,
        title = this.title,
        description = this.description,
    )

}


