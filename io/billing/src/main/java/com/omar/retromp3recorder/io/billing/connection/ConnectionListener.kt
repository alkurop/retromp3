package com.omar.retromp3recorder.io.billing.connection

import com.android.billingclient.api.*
import com.omar.retromp3recorder.io.billing.BillingError
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.ifNotFailed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference

internal class ConnectionListener : BillingClientStateListener, PurchasesUpdatedListener {
    private val _connectionState =
        MutableStateFlow<BillingConnectionState>(BillingConnectionState.Loading)
    val connectionState: StateFlow<BillingConnectionState> = _connectionState

    private val _purchaseUpdateFlow = MutableSharedFlow<PurchaseUpdateData>(
        replay = 0
    )

    private val purchaseCache = AtomicReference(PurchaseUpdateData())

    val purchaseUpdateFlow: Flow<PurchaseUpdateData> = _purchaseUpdateFlow

    override fun onBillingServiceDisconnected() {
        _connectionState.value = BillingConnectionState.Disconnected()
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            _connectionState.value = BillingConnectionState.Connected
        } else {
            _connectionState.value = BillingConnectionState.Disconnected()
            Timber.e(billingResult.debugMessage)
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchaseList: MutableList<Purchase>?) {
        updatePurchaseCache(result.ifNotFailed { purchaseList ?: emptyList() })
        _purchaseUpdateFlow.tryEmit(purchaseCache.get())
    }

    fun setLoading() {
        _connectionState.value = BillingConnectionState.Loading
    }

    fun setDisconnected() {
        _connectionState.value = BillingConnectionState.Disconnected()
    }

    fun updatePurchaseCache(updateData: Result<List<Purchase>>) {
        purchaseCache.getAndUpdate { it.update(updateData) }
    }

}

sealed class BillingConnectionState {
    object Loading : BillingConnectionState()
    object Connected : BillingConnectionState()
    data class Disconnected(val cause: Throwable = BillingError.ConnectionError("Disconnected")) :
        BillingConnectionState()
}


internal data class PurchaseUpdateData(
    val purchaseList: List<Purchase> = emptyList(),
    val addedItems: List<Purchase> = emptyList(),
    val error: Throwable? = null
)

internal fun PurchaseUpdateData.update(updateData: Result<List<Purchase>>): PurchaseUpdateData {
    val currentList = this.purchaseList
    return if (updateData.isFailure) {
        PurchaseUpdateData(currentList, error = updateData.exceptionOrNull())
    } else {
        val addedItemList =
            updateData.getOrNull()?.filter { currentList.contains(it).not() } ?: emptyList()
        PurchaseUpdateData(currentList, addedItemList)
    }
}


