package com.omar.retromp3recorder.io.billing

import android.app.Activity
import com.android.billingclient.api.*
import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.connection.BillingConnection
import com.omar.retromp3recorder.io.billing.connection.ConnectUC
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toAcknowledgeParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toBillingFlowParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toConsumeParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toProductQueryParams
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toProductListResult
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toPurchasesListResult
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toResult
import com.omar.retromp3recorder.io.billing.mapping.toDomainModel
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

internal class BillingImpl @Inject constructor(
    private val connection: BillingConnection,
    private val connectUC: ConnectUC,
    private val scopeJobWrapper: ScopeJobWrapper,
) : Billing {
    private val productDataCache = AtomicReference(emptyList<ProductDetails>())

    override suspend fun uiLaunchBillingFlow(
        activity: Activity, product: ProductData
    ): Result<PurchaseData> {
        val requestItem =
            productDataCache.get().firstOrNull { it.productId == product.productType.productId }

        return requestItem?.let { productDetails ->
            withConnection {
                launchBillingFlow(activity, productDetails.toBillingFlowParams())
                connection.purchaseFlow().first().toResult()
            }
        } ?: Result.failure(
            BillingError.OtherError(
                "Product not found in cache with id ${product.productType}, cache size was ${productDataCache.get().size}"
            )
        )
    }

    override suspend fun getProductDetails(productIdList: List<String>): Result<List<ProductData>> =
        withConnection {
            queryProductDetails(productIdList.toProductQueryParams()).toProductListResult()
                .also { it.getOrNull()?.let { productList -> productDataCache.set(productList) } }
                .map { it.mapNotNull { item -> item.toDomainModel() } }

        }

    override suspend fun getUserActivePurchases(): Result<List<PurchaseData>> = withConnection {
        queryPurchasesAsync(RequestMapper.createPurchasesParams()).toPurchasesListResult()
            .map { resultList ->
                connection.updatePurchaseList(resultList)
                resultList.mapNotNull { item -> item.toDomainModel() }
            }
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
