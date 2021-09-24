package com.omar.retromp3recorder.app.di

import android.app.Application
import android.content.Context
import com.omar.retromp3recorder.utils.ServiceDealer
import dagger.Module
import dagger.Provides


@Module
class AndroidModule(private val app: Application) {

    @Provides
    fun context(): Context {
        return app
    }

    @Provides
    fun provideWakelockDealer(serviceDealerImpl: com.omar.retromp3recorder.app.ServiceDealerImpl): ServiceDealer =
        serviceDealerImpl
}