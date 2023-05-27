package com.omar.retromp3recorder.io.downloader;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J$\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016J\u0014\u0010\f\u001a\u00020\r*\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nH\u0002J \u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006*\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/omar/retromp3recorder/io/downloader/RetrofitFileDownloader;", "Lcom/omar/retromp3recorder/io/downloader/FileDownloader;", "fileApi", "Lcom/omar/retromp3recorder/io/downloader/FileApi;", "(Lcom/omar/retromp3recorder/io/downloader/FileApi;)V", "downloadLargeFile", "Lkotlinx/coroutines/flow/Flow;", "Lcom/omar/retromp3recorder/utils/domain/LoadingState;", "Ljava/io/File;", "sourcePath", "", "destinationPath", "saveFile", "", "Lokhttp3/ResponseBody;", "destination", "saveFileFlow", "downloader_debug"})
public final class RetrofitFileDownloader implements com.omar.retromp3recorder.io.downloader.FileDownloader {
    private final com.omar.retromp3recorder.io.downloader.FileApi fileApi = null;
    
    @javax.inject.Inject()
    public RetrofitFileDownloader(@org.jetbrains.annotations.NotNull()
    com.omar.retromp3recorder.io.downloader.FileApi fileApi) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public kotlinx.coroutines.flow.Flow<com.omar.retromp3recorder.utils.domain.LoadingState<java.io.File>> downloadLargeFile(@org.jetbrains.annotations.NotNull()
    java.lang.String sourcePath, @org.jetbrains.annotations.NotNull()
    java.lang.String destinationPath) {
        return null;
    }
    
    @kotlin.Suppress(names = {"UNUSED"})
    private final void saveFile(okhttp3.ResponseBody $this$saveFile, java.lang.String destination) {
    }
    
    private final kotlinx.coroutines.flow.Flow<com.omar.retromp3recorder.utils.domain.LoadingState<java.io.File>> saveFileFlow(okhttp3.ResponseBody $this$saveFileFlow, java.lang.String destination) {
        return null;
    }
}