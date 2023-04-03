package com.omar.retromp3recorder.bl.billing.actions

import com.omar.retromp3recorder.domain.*
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.io.billing.mapping.findProduct
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.domain.toOptional
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class BuyProductUC @Inject constructor(
    private val billing: Billing,
    private val listProducts: ListAvailableProductsUC,
    private val listPurchasesUC: ListPurchasesUC,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(productId: ProductId): Result<PurchaseData> {
        return withContext(scopeJobWrapper.coroutineContext) {
            Timber.d("BILLING Product trying to buy $productId")

            // find existing purchases first
            listPurchasesUC.execute()
                .map { it.firstOrNull { purchase -> purchase.productId == productId }.toOptional() }
                .chainSuspend { optionalPurchaseData ->
                    val unconsumedPurchase = optionalPurchaseData.value
                    if (unconsumedPurchase != null && unconsumedPurchase.isAcknowledged.not()) {
                        billing.postAcknowledgePurchase(unconsumedPurchase)
                            .map { unconsumedPurchase }
                            .onSuccess {
                                Timber.d("BILLING Product acknowledged from previous buy attempt $it")
                            }
                    } else if (unconsumedPurchase != null) {
                        Timber.d("BILLING Product using unconsumed product instead of buying it again $unconsumedPurchase")
                        unconsumedPurchase.toResult()
                    } else {
                        listProducts.execute()
                            .chain {
                                it.findProduct(productId)
                            }
                            .chainSuspend { billing.uiLaunchBillingFlow(it) }
                            .chainSuspend { purchase ->
                                billing.postAcknowledgePurchase(purchase).map { purchase }
                                    .onSuccess {
                                        Timber.d("BILLING Product bought and acknowledged $it")
                                    }
                            }
                    }
                }
        }
    }
}
