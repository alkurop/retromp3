package com.omar.retromp3recorder.io.downloader;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u0005H\'\u00a8\u0006\u0006"}, d2 = {"Lcom/omar/retromp3recorder/io/downloader/FileApi;", "", "getFile", "Lokhttp3/ResponseBody;", "url", "", "downloader_debug"})
public abstract interface FileApi {
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET()
    @retrofit2.http.Streaming()
    public abstract okhttp3.ResponseBody getFile(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Url()
    java.lang.String url);
}