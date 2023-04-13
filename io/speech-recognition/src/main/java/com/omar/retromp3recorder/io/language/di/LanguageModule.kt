package com.omar.retromp3recorder.io.language.di

import com.omar.retromp3recorder.io.language.LanguageLister
import com.omar.retromp3recorder.io.language.VoskLanguageLister
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal interface LanguageModule {
    @Binds
    fun bindLanguageLister(instance: VoskLanguageLister): LanguageLister
}
