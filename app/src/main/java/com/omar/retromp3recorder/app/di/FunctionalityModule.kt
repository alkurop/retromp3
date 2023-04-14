package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorderLame
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.share.SharerImpl
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
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
    fun provideVoiceRecorderBase(instance: Mp3VoiceRecorderLame): Mp3VoiceRecorder

    @Singleton
    @Binds
    fun provideSharingModuleBase(clazz: SharerImpl): Sharer

}

@InstallIn(SingletonComponent::class)
@Module
class FunctionalityProvidesModule {

    @Provides
    fun provideProjectionUnsubscriber(audioCoroutineContext: AudioCoroutineContext): MediaProjectionUnsubscriber {
        return MediaProjectionUnsubscriber(audioCoroutineContext)
    }

}

