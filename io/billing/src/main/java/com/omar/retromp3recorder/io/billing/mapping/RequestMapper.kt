package com.omar.retromp3recorder.io.billing.mapping

import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.omar.retromp3recorder.domain.PurchaseData

internal object RequestMapper {

    fun List<String>.toProductQueryParams() = QueryProductDetailsParams.newBuilder()
        .setProductList(
            this.map {
                QueryProductDetailsParams.Product.newBuilder().setProductId(it)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            }
        ).build()

    fun createPurchasesParams() = QueryPurchasesParams.newBuilder()
        .setProductType(BillingClient.ProductType.INAPP)
        .build()

    fun PurchaseData.toAcknowledgeParams() = AcknowledgePurchaseParams.newBuilder()
        .setPurchaseToken(this.token)
        .build()

    fun PurchaseData.toConsumeParams() =
        ConsumeParams.newBuilder().setPurchaseToken(this.token).build()

    fun ProductDetails.toBillingFlowParams() = BillingFlowParams.newBuilder()
        .setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(this)
                    .build()
            )
        )
        .build()
}
