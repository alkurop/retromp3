package com.omar.retromp3recorder.io.billing.connection

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ConnectionState.*
import com.android.billingclient.api.InAppMessageParams
import com.android.billingclient.api.InAppMessageResult
import com.android.billingclient.api.Purchase
import com.omar.retromp3recorder.domain.toResult
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class BillingConnection @Inject constructor(
    @ActivityContext private val context: Context,
    private val scopeJobWrapper: ScopeJobWrapper,
    private val connectUC: ConnectUC,
    private val billingClientProvider: BillingClientProvider,
    private val billingConnectionListener: BillingConnectionListener
) {
    private var billingClient: BillingClient = createClient()
    private val activity: Activity get() = context as Activity

    val connectionFlow: Flow<BillingConnectionState> get() = billingConnectionListener.connectionState
    val purchaseFlow: Flow<PurchaseUpdateData> get() = billingConnectionListener.purchaseUpdateFlow

    fun updatePurchaseList(resultList: List<Purchase>) =
        billingConnectionListener.updatePurchaseCache(resultList.toResult())

    suspend fun <T> executeWithConnection(action: suspend BillingClient.() -> Result<T>): Result<T> =
        withContext(scopeJobWrapper.coroutineContext) {
            if (billingClient.isReady.not()) {
                val connectionResult = connectUC.execute(this@BillingConnection)
                if (connectionResult.isFailure) {
                    return@withContext Result.failure(connectionResult.exceptionOrNull()!!)
                }
            }
            execute { action() }
        }

    suspend fun subscribeToMessages(
        params: InAppMessageParams,
        listener: (InAppMessageResult) -> Unit
    ) {
        executeWithConnection {
            showInAppMessages(activity, params, listener)
            Unit.toResult()
        }
    }

    private fun createClient(): BillingClient =
        billingClientProvider.provideNewClientBuilder().setListener(billingConnectionListener)
            .build()

    fun connect() {
        Timber.d("BILLING trying to connect")
        val state = billingClient.connectionState
        if (state == CLOSED) {
            billingClient = createClient()
        } else if (state == CONNECTED || state == CONNECTING) {
            return
        }
        billingConnectionListener.setLoading()
        billingClient.startConnection(billingConnectionListener)
    }


    fun disconnect() {
        billingConnectionListener.setDisconnected()
        billingClient.endConnection()
    }


    private suspend fun <T> execute(function: suspend BillingClient.() -> T): T =
        function.invoke(billingClient)
}
