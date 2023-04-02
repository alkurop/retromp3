package com.omar.retromp3recorder.io.billing.connection

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ConnectionState.*
import com.android.billingclient.api.InAppMessageParams
import com.android.billingclient.api.InAppMessageResult
import com.android.billingclient.api.Purchase
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toResult
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class BillingConnectionImpl @Inject constructor(
    @ActivityContext private val context: Context,
) : BillingConnection {

    private val billingConnectionListener = ConnectionListener()
    private var billingClient: BillingClient = createClient()

    override val isReady: Boolean
        get() = billingClient.isReady

    private val activity: Activity
        get() = context as Activity

    override fun connectionFlow(): Flow<BillingConnectionState> {
        return billingConnectionListener.connectionState
    }

    override fun purchaseFlow(): Flow<PurchaseUpdateData> {
        return billingConnectionListener.purchaseUpdateFlow
    }

    override fun updatePurchaseList(resultList: List<Purchase>) {
        billingConnectionListener.updatePurchaseCache(resultList.toResult())
    }

    override fun subscribeToMessages(params: InAppMessageParams, listener: (InAppMessageResult) -> Unit) {
        billingClient.showInAppMessages(activity, params, listener)
    }

    override fun connect() {
        val state = billingClient.connectionState
        if (state == CLOSED) {
            billingClient = createClient()
        } else if (state == CONNECTED || state == CONNECTING) {
            return
        }
        billingConnectionListener.setLoading()
        billingClient.startConnection(billingConnectionListener)
    }

    private fun createClient(): BillingClient {
        return BillingClient.newBuilder(context).enablePendingPurchases()
            .setListener(billingConnectionListener).build()
    }

    override fun disconnect() {
        billingConnectionListener.setDisconnected()
        billingClient.endConnection()
    }

    override suspend fun <T> execute(function: suspend BillingClient.() -> T): T =
        function.invoke(billingClient)

}
