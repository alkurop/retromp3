package com.omar.retromp3recorder.io.billing

import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class BillingListener() : BillingClientStateListener, PurchasesUpdatedListener {
    private val _connectionState = MutableStateFlow(BillingConnectionState.Loading)
    val connectionState: StateFlow<BillingConnectionState> = _connectionState

//    private val _purchaseUpdateFlow = MutableSharedFlow<List<Purchase>>(replay = 1)
//    val purchaseUpdateFlow: Flow<List<Purchase>> = _purchaseUpdateFlow

    override fun onBillingServiceDisconnected() {
        _connectionState.value = BillingConnectionState.Disconnected
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            _connectionState.value = BillingConnectionState.Connected
        } else {
            _connectionState.value = BillingConnectionState.Disconnected
            Timber.e(billingResult.debugMessage)
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchaseList: MutableList<Purchase>?) {
//        _purchaseUpdateFlow.tryEmit(purchaseList ?: emptyList())
    }

    fun setLoading() {
        _connectionState.value = BillingConnectionState.Loading
    }

    fun setDisconnected(){}
}

enum class BillingConnectionState {
    Loading,
    Connected,
    Disconnected
}
