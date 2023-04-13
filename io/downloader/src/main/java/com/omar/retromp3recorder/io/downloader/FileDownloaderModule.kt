package com.omar.retromp3recorder.io.downloader

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface FileDownloaderModule {
    @Singleton
    @Binds
    fun bind(instance: RetrofitFileDownloader): FileDownloader
}

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder().build()
        return Retrofit.Builder()
            .baseUrl("no_bas_url")
            .client(client)
            .build()
    }

    @Provides
    fun provideFileApi(retrofit: Retrofit): FileApi {
        return retrofit.create(FileApi::class.java)
    }
}
