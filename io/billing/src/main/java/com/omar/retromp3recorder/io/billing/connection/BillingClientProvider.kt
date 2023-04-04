package com.omar.retromp3recorder.io.billing.connection

import android.content.Context
import com.android.billingclient.api.BillingClient
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class BillingClientProvider @Inject constructor(
    @ActivityContext private val context: Context
) {
    fun provideNewClientBuilder(): BillingClient.Builder {
        return BillingClient.newBuilder(context).enablePendingPurchases()
    }
}
