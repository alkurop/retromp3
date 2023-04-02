package com.omar.retromp3recorder.io.billing.connection

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.InAppMessageParams
import com.android.billingclient.api.InAppMessageResult
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.Flow


interface BillingConnection {

    fun connect()

    fun disconnect()

    fun connectionFlow(): Flow<BillingConnectionState>

    fun purchaseFlow(): Flow<PurchaseUpdateData>

    suspend fun <T> execute(function: suspend BillingClient.() -> T): T

    fun updatePurchaseList(resultList: List<Purchase>)

    val isReady: Boolean
    fun subscribeToMessages(params: InAppMessageParams, listener: (InAppMessageResult) -> Unit)
}





