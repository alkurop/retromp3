package com.omar.retromp3recorder.io.billing.di

import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.io.billing.BillingImpl
import com.omar.retromp3recorder.utils.domain.DifferedCoroutineScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNUSED")
@InstallIn(ActivityComponent::class)
@Module
internal interface BillingModule {

    @ActivityScoped
    @Binds
    fun bindBilling(instance: BillingImpl): Billing

}


@InstallIn(SingletonComponent::class)
@Module
internal class BillingProviderModule {
    @Singleton
    @Provides
    fun provideBillingScopeJobWrapper(dispatcher: CoroutineDispatcher): BillingCoroutineScope =
        BillingCoroutineScope(dispatcher)

}

// Should not cancel billing jobs in case view disappeared
class BillingCoroutineScope @Inject constructor(dispatcher: CoroutineDispatcher) :
    DifferedCoroutineScope(dispatcher)
