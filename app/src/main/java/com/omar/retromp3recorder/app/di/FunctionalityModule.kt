package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorderImpl
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.share.SharerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal interface FunctionalityModule {
    @Binds
    fun provideVoiceRecorderBase(instance: Mp3VoiceRecorderImpl): Mp3VoiceRecorder

    @Binds
    fun provideSharingModuleBase(clazz: SharerImpl): Sharer

}
