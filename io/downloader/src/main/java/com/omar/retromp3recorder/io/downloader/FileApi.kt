package com.omar.retromp3recorder.io.downloader

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileApi {
    @Streaming
    @GET
    suspend fun getFile(@Url url: String): ResponseBody
}
