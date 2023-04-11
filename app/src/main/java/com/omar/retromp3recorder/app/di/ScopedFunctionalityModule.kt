package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.AudioPlayerExo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface ScopedFunctionalityModule {
    @Singleton
    @Binds
    fun provideAudioPlayer(clazz: AudioPlayerExo): AudioPlayer
}
