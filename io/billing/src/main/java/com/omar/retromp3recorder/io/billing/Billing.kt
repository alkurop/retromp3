package com.omar.retromp3recorder.io.billing

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.domain.PurchaseData

/**
 * Should be injected in activity
 */
interface Billing {

    suspend fun getAvailableProducts(productIdList: List<String>): Result<List<ProductId>>

    suspend fun getUserActivePurchases(): Result<List<PurchaseData>>

    suspend fun postAcknowledgePurchase(purchase: PurchaseData): Result<Unit>

    suspend fun postConsumePurchase(purchase: PurchaseData): Result<Unit>

    suspend fun uiLaunchBillingFlow(productId: ProductId): Result<PurchaseData>

    fun disconnect()
}
