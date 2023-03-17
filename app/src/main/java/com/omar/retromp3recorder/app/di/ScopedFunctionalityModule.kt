package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.AudioPlayerExoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
internal interface ScopedFunctionalityModule {
    @Binds
    fun provideAudioPlayer(clazz: AudioPlayerExoImpl): AudioPlayer
}
