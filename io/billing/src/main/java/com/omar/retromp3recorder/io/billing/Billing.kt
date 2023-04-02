package com.omar.retromp3recorder.io.billing

import android.app.Activity
import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.domain.PurchaseData

interface Billing {

    suspend fun getProductDetails(productIdList: List<String>): Result<List<ProductData>>

    suspend fun getUserActivePurchases(): Result<List<PurchaseData>>

    suspend fun postAcknowledgePurchase(purchase: PurchaseData): Result<Unit>

    suspend fun postConsumePurchase(purchase: PurchaseData): Result<Unit>

    suspend fun uiLaunchBillingFlow(activity: Activity, product: ProductData): Result<Unit>

    suspend fun uiShowMessage() {}

    fun disconnect()
}
