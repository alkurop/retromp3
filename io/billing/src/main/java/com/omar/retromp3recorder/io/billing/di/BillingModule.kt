package com.omar.retromp3recorder.io.billing.di

import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.io.billing.BillingImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Suppress("UNUSED")
@InstallIn(ActivityComponent::class)
@Module
internal interface BillingModule {

    @ActivityScoped
    @Binds
    fun bindBilling(instance: BillingImpl): Billing

}
