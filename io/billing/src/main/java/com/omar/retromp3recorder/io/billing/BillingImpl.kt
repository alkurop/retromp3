package com.omar.retromp3recorder.io.billing

import com.android.billingclient.api.*
import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toAcknowledgeParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toConsumeParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toProductQueryParams
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toProductListResult
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toPurchasesListResult
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toResult
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class BillingImpl @Inject constructor(
    private val connection: BillingConnection,
    private val connectUC: ConnectUC,
    private val scopeJobWrapper: ScopeJobWrapper,
) : Billing {
    override suspend fun getProductDetails(productIdList: List<String>): Result<List<ProductData>> =
        withConnection {
            queryProductDetails(productIdList.toProductQueryParams()).toProductListResult()
        }

    override suspend fun getUserActivePurchases(): Result<List<PurchaseData>> =
        withConnection {
            queryPurchasesAsync(RequestMapper.createPurchasesParams()).toPurchasesListResult()
        }

    override suspend fun postAcknowledgePurchase(purchase: PurchaseData): Result<Unit> =
        withConnection {
            acknowledgePurchase(purchase.toAcknowledgeParams()).toResult()
        }


    override suspend fun postConsumePurchase(purchase: PurchaseData): Result<Unit> =
        withConnection {
            consumePurchase(purchase.toConsumeParams()).billingResult.toResult()
        }

    private suspend fun <T> withConnection(doWhenConnected: suspend BillingClient.() -> Result<T>): Result<T> =
        withContext(scopeJobWrapper.coroutineContext) {
            val ready = connection.isReady
            if (ready.not()) {
                val connectionResult = connectUC.execute()
                if (connectionResult.isFailure) {
                    return@withContext Result.failure(connectionResult.exceptionOrNull()!!)
                }
            }
            connection.execute { doWhenConnected() }
        }


    override fun disconnect() = connection.disconnect()
}
