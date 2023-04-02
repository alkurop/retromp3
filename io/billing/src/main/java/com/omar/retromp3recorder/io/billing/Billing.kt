package com.omar.retromp3recorder.io.billing

import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.domain.PurchaseData
import kotlinx.coroutines.flow.Flow

/**
 * Should be injected in activity
 */
interface Billing {

    suspend fun getProductDetails(productIdList: List<String>): Result<List<ProductData>>

    suspend fun getUserActivePurchases(): Result<List<PurchaseData>>

    suspend fun postAcknowledgePurchase(purchase: PurchaseData): Result<Unit>

    suspend fun postConsumePurchase(purchase: PurchaseData): Result<Unit>

    suspend fun uiLaunchBillingFlow(product: ProductData): Result<PurchaseData>

    fun messageFlow(): Flow<String>

    fun disconnect()
    suspend fun subscribeToMessages(): Result<Unit>
}
