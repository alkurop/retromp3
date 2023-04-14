package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.utils.domain.Constants
import com.omar.retromp3recorder.utils.domain.DifferedCoroutineScope
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class AndroidModule {

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

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Named("main")
    @Provides
    fun provideMainThreadDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Named("main")
    @Provides
    fun provideMainThreadScopeJobWrapper(@Named("main") dispatcher: CoroutineDispatcher): DifferedCoroutineScope =
        DifferedCoroutineScope(dispatcher)


    @Provides
    fun provideScopeJobWrapper(dispatcher: CoroutineDispatcher): DifferedCoroutineScope =
        DifferedCoroutineScope(dispatcher)

}
