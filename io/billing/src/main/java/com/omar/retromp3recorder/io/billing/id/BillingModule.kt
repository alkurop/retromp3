package com.omar.retromp3recorder.io.billing.id

import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.io.billing.BillingImpl
import com.omar.retromp3recorder.io.billing.connection.BillingConnection
import com.omar.retromp3recorder.io.billing.connection.BillingConnectionImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
internal interface BillingModule {

    @Binds
    fun bindBilling(instance: BillingImpl): Billing

    @Binds
    fun bindConnection(instance: BillingConnectionImpl): BillingConnection
}
