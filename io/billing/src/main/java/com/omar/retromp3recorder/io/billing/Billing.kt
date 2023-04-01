package com.omar.retromp3recorder.io.billing

import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toProductQueryParams
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toProductListResult
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toPurchasesListResult
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface Billing {

    suspend fun queryProductDetails(productIdList: List<String>): Result<List<ProductData>>

    suspend fun queryUserActivePurchases(): Result<List<PurchaseData>>

    suspend fun acknowledgePurchase()

    fun consumePurchase() {}

    fun launchBillingFlow() {}

    fun showMessage() {}

    fun disconnect()
}

internal class BillingImpl @Inject constructor(
    private val connection: BillingConnection,
    private val connectUC: ConnectUC,
    private val scopeJobWrapper: ScopeJobWrapper,
) : Billing {

    override suspend fun queryProductDetails(productIdList: List<String>): Result<List<ProductData>> =
        withConnection {
            queryProductDetails(productIdList.toProductQueryParams())
                .toProductListResult()
        }

    override suspend fun queryUserActivePurchases(): Result<List<PurchaseData>> = withConnection {
        queryPurchasesAsync(RequestMapper.getActivePurchasesParams())
            .toPurchasesListResult()
    }

    override suspend fun acknowledgePurchase() {
        withConnection {
            val params = AcknowledgePurchaseParams.newBuilder().build()
            acknowledgePurchase(params)

            Result.success(0)
        }
    }


    private suspend fun <T> withConnection(doWhenConnected: suspend BillingClient.() -> Result<T>): Result<T> {
        return withContext(scopeJobWrapper.coroutineContext) {
            val ready = connection.isReady
            if (ready.not()) {
                val connectionResult = connectUC.execute()
                if (connectionResult.isFailure) {
                    return@withContext Result.failure(connectionResult.exceptionOrNull()!!)
                }
            }
            connection.execute { doWhenConnected() }
        }
    }

    override fun disconnect() = connection.disconnect()
}
