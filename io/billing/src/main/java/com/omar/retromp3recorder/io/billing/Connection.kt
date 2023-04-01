package com.omar.retromp3recorder.io.billing

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ConnectionState.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal interface BillingConnection {

    fun connect()

    fun disconnect()

    fun connectionFlow(): Flow<BillingConnectionState>

    suspend fun <T> execute(function: suspend BillingClient.() -> T): T

    val isReady: Boolean
}


internal class BillingConnectionImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : BillingConnection {

    private val billingListener = Listener()
    private var billingClient: BillingClient = createClient()


    override val isReady: Boolean
        get() = billingClient.isReady


    override fun connectionFlow(): Flow<BillingConnectionState> {
        return billingListener.connectionState
    }

    override fun connect() {
        val state = billingClient.connectionState
        if (state == CLOSED) {
            billingClient = createClient()
        } else if (state == CONNECTED || state == CONNECTING) {
            return
        }
        billingListener.setLoading()
        billingClient.startConnection(billingListener)
    }

    private fun createClient(): BillingClient {
        return BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener(billingListener)
            .build()
    }

    override fun disconnect() {
        billingListener.setDisconnected()
        billingClient.endConnection()
    }

    override suspend fun <T> execute(function: suspend BillingClient.() -> T): T =
        function.invoke(billingClient)

}


