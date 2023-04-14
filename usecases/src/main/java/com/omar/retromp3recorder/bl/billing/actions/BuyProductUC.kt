package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.io.billing.mapping.findProduct
import com.omar.retromp3recorder.utils.domain.chain
import com.omar.retromp3recorder.utils.domain.chainSuspend
import com.omar.retromp3recorder.utils.domain.toOptional
import com.omar.retromp3recorder.utils.domain.toResult
import timber.log.Timber
import javax.inject.Inject

class BuyProductUC @Inject constructor(
    private val billing: Billing,
    private val listProducts: ListAvailableProductsUC,
    private val listPurchasesUC: ListPurchasesUC,
) {
    suspend fun execute(productId: ProductId): Result<PurchaseData> {
        Timber.d("BILLING Product trying to buy $productId")

        // find existing purchases first
        return listPurchasesUC.execute()
            .map { it.firstOrNull { purchase -> purchase.productId == productId }.toOptional() }
            .chainSuspend { optionalPurchaseData ->
                val unconsumedPurchase = optionalPurchaseData.value
                if (unconsumedPurchase != null && unconsumedPurchase.isAcknowledged.not()) {
                    unconsumedPurchase.toResult().postAcknowledge()
                } else if (unconsumedPurchase != null) {
                    Timber.d("BILLING Product using unconsumed product instead of buying it again $unconsumedPurchase")
                    unconsumedPurchase.toResult()
                } else {
                    listProducts.execute()
                        .chain { it.findProduct(productId) }
                        .chainSuspend { billing.uiLaunchBillingFlow(it) }
                        .postAcknowledge()
                }
            }
    }


    private suspend fun Result<PurchaseData>.postAcknowledge(): Result<PurchaseData> =
        this.chainSuspend { purchase ->
            billing.postAcknowledgePurchase(purchase).map { purchase }
                .onSuccess {
                    Timber.d("BILLING Product bought and acknowledged $it")
                }
        }
}
