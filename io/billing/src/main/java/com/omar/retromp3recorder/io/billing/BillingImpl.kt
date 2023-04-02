package com.omar.retromp3recorder.io.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.omar.retromp3recorder.domain.ProductData
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.connection.BillingConnection
import com.omar.retromp3recorder.io.billing.connection.ConnectUC
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.buildMessageParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toAcknowledgeParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toBillingFlowParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toConsumeParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toProductQueryParams
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toProductListResult
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toPurchasesListResult
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toResult
import com.omar.retromp3recorder.io.billing.mapping.toDomainModel
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

internal class BillingImpl @Inject constructor(
    private val connection: BillingConnection,
    private val connectUC: ConnectUC,
    private val scopeJobWrapper: ScopeJobWrapper,
    @ActivityContext private val context: Context,
) : Billing {

    private val activity: Activity
        get() = context as Activity

    private val productDataCache = AtomicReference(emptyList<ProductDetails>())
    private val messageFlow = MutableSharedFlow<String>()
    private val messageListener: (InAppMessageResult) -> Unit = {
        when (it.responseCode) {
            InAppMessageResult.InAppMessageResponseCode.NO_ACTION_NEEDED -> {
                // we don't do much here
            }
            else -> {
                // nothing here either
            }
        }
    }

    override suspend fun subscribeToMessages(): Result<Unit> =
        withConnection {
            connection.subscribeToMessages(buildMessageParams(), messageListener)
            Result.success(Unit)
        }

    override suspend fun uiLaunchBillingFlow(
        product: ProductData
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

    override fun messageFlow(): Flow<String> {
        return messageFlow
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
