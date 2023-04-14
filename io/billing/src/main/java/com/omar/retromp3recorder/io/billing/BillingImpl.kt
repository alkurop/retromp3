package com.omar.retromp3recorder.io.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.domain.PurchaseData
import com.omar.retromp3recorder.io.billing.connection.BillingConnection
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toAcknowledgeParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toBillingFlowParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toConsumeParams
import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.toProductQueryParams
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toProductListResult
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toPurchasesListResult
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.toResult
import com.omar.retromp3recorder.io.billing.mapping.toDomainModel
import com.omar.retromp3recorder.utils.domain.toResult
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

internal class BillingImpl @Inject constructor(
    private val connection: BillingConnection,
    @ActivityContext private val context: Context,
) : Billing {

    private val activity: Activity get() = context as Activity
    private val productDataCache = AtomicReference(emptyList<ProductDetails>())

    override suspend fun getAvailableProducts(productIdList: List<ProductId>): Result<List<ProductId>> {
        val cachedProducts = productDataCache.get()
        return if (cachedProducts.isNotEmpty()) cachedProducts.mapNotNull { it.toDomainModel() }
            .toResult()
        else executeOnConnection {
            queryProductDetails(productIdList.toProductQueryParams()).toProductListResult()
        }.onSuccess { productList ->
            productDataCache.set(productList)
        }.map { it.mapNotNull { item -> item.toDomainModel() } }
    }

    override suspend fun getUserActivePurchases(): Result<List<PurchaseData>> =
        executeOnConnection {
            queryPurchasesAsync(RequestMapper.createPurchasesParams()).toPurchasesListResult()
                .map { resultList ->
                    connection.updatePurchaseList(resultList)
                    resultList.mapNotNull { item -> item.toDomainModel() }
                }
        }

    override suspend fun postAcknowledgePurchase(purchase: PurchaseData): Result<Unit> =
        executeOnConnection { acknowledgePurchase(purchase.toAcknowledgeParams()).toResult() }

    override suspend fun postConsumePurchase(purchase: PurchaseData): Result<Unit> =
        executeOnConnection { consumePurchase(purchase.toConsumeParams()).billingResult.toResult() }

    override suspend fun uiLaunchBillingFlow(productId: ProductId): Result<PurchaseData> {
        val requestItem =
            productDataCache.get().firstOrNull { it.productId == productId.productId }

        return requestItem?.let { productDetails ->
            executeOnConnection {
                launchBillingFlow(activity, productDetails.toBillingFlowParams())
                connection.purchaseFlow.first().toResult().also { result ->
                    Timber.d("BILLING Purchase result after billing flow init $result")
                }
            }
        } ?: Result.failure(
            BillingError.OtherError(
                "Product not found in cache with id ${productId}, cache size was ${productDataCache.get().size}"
            )
        )
    }

    override fun disconnect() = connection.disconnect()

    private suspend fun <T> executeOnConnection(doWhenConnected: suspend BillingClient.() -> Result<T>): Result<T> =
        connection.executeWithConnection(doWhenConnected)

}
