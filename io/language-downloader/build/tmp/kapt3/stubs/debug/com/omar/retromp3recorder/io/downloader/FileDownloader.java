package com.omar.retromp3recorder.io.downloader;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H&\u00a8\u0006\t"}, d2 = {"Lcom/omar/retromp3recorder/io/downloader/FileDownloader;", "", "downloadLargeFile", "Lkotlinx/coroutines/flow/Flow;", "Lcom/omar/retromp3recorder/utils/domain/LoadingState;", "Ljava/io/File;", "sourcePath", "", "destinationPath", "downloader_debug"})
public abstract interface FileDownloader {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.omar.retromp3recorder.utils.domain.LoadingState<java.io.File>> downloadLargeFile(@org.jetbrains.annotations.NotNull()
    java.lang.String sourcePath, @org.jetbrains.annotations.NotNull()
    java.lang.String destinationPath);
}