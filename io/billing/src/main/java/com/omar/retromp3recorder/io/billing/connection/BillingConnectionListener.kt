package com.omar.retromp3recorder.io.billing.connection

import com.android.billingclient.api.*
import com.omar.retromp3recorder.io.billing.BillingError
import com.omar.retromp3recorder.io.billing.mapping.ResultMapper.ifNotFailed
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

internal class BillingConnectionListener @Inject constructor(
    private val jobWrapper: AudioCoroutineContext
) : BillingClientStateListener, PurchasesUpdatedListener {

    private val _connectionState =
        MutableStateFlow<BillingConnectionState>(BillingConnectionState.Loading)
    val connectionState: StateFlow<BillingConnectionState> = _connectionState

    private val _purchaseUpdateFlow = MutableSharedFlow<PurchaseUpdateData>(replay = 0)
    val purchaseUpdateFlow: Flow<PurchaseUpdateData> = _purchaseUpdateFlow

    private val purchaseCache = AtomicReference(PurchaseUpdateData())

    fun setLoading() {
        _connectionState.value = BillingConnectionState.Loading
    }

    fun setDisconnected() {
        _connectionState.value = BillingConnectionState.Disconnected()
    }

    fun updatePurchaseCache(updateData: Result<List<Purchase>>) {
        purchaseCache.getAndUpdate { it.update(updateData) }
    }

    override fun onBillingServiceDisconnected() {
        _connectionState.value = BillingConnectionState.Disconnected()
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            _connectionState.value = BillingConnectionState.Connected
        } else {
            _connectionState.value =
                BillingConnectionState.Disconnected(Error("Disconnected with code ${billingResult.responseCode}"))
            Timber.e("BILLING ${billingResult.debugMessage} with code ${billingResult.responseCode}")
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchaseList: List<Purchase>?) {
        updatePurchaseCache(result.ifNotFailed { purchaseList ?: emptyList() }.onSuccess {
            Timber.d("BILLING Purchase list updated $it")
        })

        jobWrapper.launch { _purchaseUpdateFlow.emit(purchaseCache.get()) }
    }

    private fun PurchaseUpdateData.update(updateData: Result<List<Purchase>>): PurchaseUpdateData {
        val currentList = this.purchaseList
        return if (updateData.isFailure) {
            PurchaseUpdateData(currentList, error = updateData.exceptionOrNull())
        } else {
            val addedItemList =
                updateData.getOrNull()?.filter { currentList.contains(it).not() } ?: emptyList()
            PurchaseUpdateData(currentList + addedItemList, addedItemList)
        }.also {
            Timber.d("BILLING Purchase state $it")
        }
    }
}

sealed class BillingConnectionState {
    object Loading : BillingConnectionState()
    object Connected : BillingConnectionState()
    data class Disconnected(val cause: Throwable = BillingError.ConnectionError("Disconnected")) :
        BillingConnectionState()
}

data class PurchaseUpdateData(
    val purchaseList: List<Purchase> = emptyList(),
    val addedItems: List<Purchase> = emptyList(),
    val error: Throwable? = null
)
