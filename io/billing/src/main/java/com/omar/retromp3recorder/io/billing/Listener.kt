package com.omar.retromp3recorder.io.billing

import com.android.billingclient.api.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

internal class Listener : BillingClientStateListener, PurchasesUpdatedListener {
    private val _connectionState =
        MutableStateFlow<BillingConnectionState>(BillingConnectionState.Loading)
    val connectionState: StateFlow<BillingConnectionState> = _connectionState

    private val _purchaseUpdateFlow = MutableStateFlow<List<Purchase>>(emptyList())

    val purchaseUpdateFlow: Flow<List<Purchase>> = _purchaseUpdateFlow

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
        _purchaseUpdateFlow.value = purchaseList ?: emptyList()
    }

    fun setLoading() {
        _connectionState.value = BillingConnectionState.Loading
    }

    fun setDisconnected() {
        _connectionState.value = BillingConnectionState.Disconnected()
    }
}

sealed class BillingConnectionState {
    object Loading : BillingConnectionState()
    object Connected : BillingConnectionState()
    data class Disconnected(val cause: Throwable = BillingError.ConnectionError("Disconnected")) :
        BillingConnectionState()
}
