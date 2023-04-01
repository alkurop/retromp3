package com.omar.retromp3recorder.io.billing

import android.content.Context
import com.android.billingclient.api.*
import com.omar.retromp3recorder.domain.Product
import com.omar.retromp3recorder.io.billing.ProductDetailsResultMapper.toProductQueryParams
import com.omar.retromp3recorder.io.billing.ProductDetailsResultMapper.toProductResult
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingWrapper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val billingListener = BillingListener()
    private val billingClient by lazy {
        BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener(billingListener)
            .build()
    }

    fun connect() {
        billingListener.setLoading()
        billingClient.startConnection(billingListener)
    }

    fun disconnect() {
        billingListener.setDisconnected()
        billingClient.endConnection()
    }


    private suspend fun queryProductDetails(
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


