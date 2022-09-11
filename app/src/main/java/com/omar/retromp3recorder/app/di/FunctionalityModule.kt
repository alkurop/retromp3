package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.AmplitudaDealerImpl
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.AudioPlayerExoImpl
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorderImpl
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.share.SharerImpl
import com.omar.retromp3recorder.utils.AmplitudaDealer
import dagger.Binds
import dagger.Module

@Module
internal interface FunctionalityModule {
    @Binds
    fun provideAudioPlayerBase(clazz: AudioPlayerExoImpl): AudioPlayer

    @Binds
    fun provideVoiceRecorderBase(clazz: Mp3VoiceRecorderImpl): Mp3VoiceRecorder

    @Binds
    fun provideSharingModuleBase(clazz: SharerImpl): Sharer

    @Binds
    fun provideAmplitudaDealer(clazz: AmplitudaDealerImpl): AmplitudaDealer

}
