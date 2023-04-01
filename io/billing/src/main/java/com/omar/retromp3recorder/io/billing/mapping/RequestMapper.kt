package com.omar.retromp3recorder.io.billing.mapping

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams

internal object RequestMapper {

    fun List<String>.toProductQueryParams(): QueryProductDetailsParams {
        return QueryProductDetailsParams.newBuilder().setProductList(
            this.map {
                QueryProductDetailsParams.Product.newBuilder().setProductId(it)
                    .setProductType(BillingClient.ProductType.INAPP).build()
            }
        ).build()
    }

    fun getActivePurchasesParams(): QueryPurchasesParams {
        return QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP)
            .build()
    }
}
