package com.omar.retromp3recorder.app.di

import android.app.Application
import android.content.Context
import com.omar.retromp3recorder.utils.domain.Constants
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Named

@Module
class AndroidModule(private val app: Application) {

    @Provides
    fun context(): Context {
        return app
    }

    @Provides
    fun provideWakelockDealer(serviceDealerImpl: ServiceDealerImpl): ServiceDealer =
        serviceDealerImpl

    @Provides
    fun provideScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Named(Constants.MAIN_THREAD)
    fun provideMainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
