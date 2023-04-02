package com.omar.retromp3recorder.io.billing.id

import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.io.billing.connection.BillingConnection
import com.omar.retromp3recorder.io.billing.connection.BillingConnectionImpl
import com.omar.retromp3recorder.io.billing.BillingImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface BillingModule {

    @Singleton
    @Binds
    fun bindBilling(instance: BillingImpl): Billing


    @Singleton
    @Binds
    fun bindConnection(instance: BillingConnectionImpl): BillingConnection
}
