package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorderImpl
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.share.SharerImpl
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.platform.MediaProjectionUnsubscriber
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface FunctionalityModule {

    @Singleton
    @Binds
    fun provideVoiceRecorderBase(instance: Mp3VoiceRecorderImpl): Mp3VoiceRecorder

    @Singleton
    @Binds
    fun provideSharingModuleBase(clazz: SharerImpl): Sharer

}

@InstallIn(SingletonComponent::class)
@Module
class FunctionalityProvidesModule {

    @Provides
    fun provideProjectionUnsubscriber(scopeJobWrapper: ScopeJobWrapper): MediaProjectionUnsubscriber {
        return MediaProjectionUnsubscriber(scopeJobWrapper)
    }

}

