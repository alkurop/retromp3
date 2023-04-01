package com.omar.retromp3recorder.io.billing

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.queryProductDetails
import com.omar.retromp3recorder.domain.Product
import com.omar.retromp3recorder.io.billing.ProductDetailsResultMapper.toProductQueryParams
import com.omar.retromp3recorder.io.billing.ProductDetailsResultMapper.toProductResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


interface Billing {

    fun connect()

    fun disconnect()

    suspend fun queryProductDetails(productIdList: List<String>): Result<List<Product>>

    fun connectionFlow(): Flow<BillingConnectionState>
}


@Singleton
class BillingImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : Billing {

    private val billingListener = BillingListener()
    private val billingClient by lazy {
        BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener(billingListener)
            .build()
    }

    override fun connectionFlow(): Flow<BillingConnectionState> {
        return billingListener.connectionState
    }

    override fun connect() {
        billingListener.setLoading()
        billingClient.startConnection(billingListener)
    }

    override fun disconnect() {
        billingListener.setDisconnected()
        billingClient.endConnection()
    }


    override suspend fun queryProductDetails(
        productIdList: List<String>
    ): Result<List<Product>> {
        return if (billingClient.isReady.not()) {
            val error = BillingError.ConnectionError("Client is not ready")
            Timber.e(error)
            Result.failure(error)
        } else {
            return billingClient.queryProductDetails(productIdList.toProductQueryParams())
                .toProductResult()
        }
    }
}


