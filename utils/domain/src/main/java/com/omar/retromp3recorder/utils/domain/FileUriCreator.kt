package com.omar.retromp3recorder.utils.domain

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import javax.inject.Inject

class FileUriCreator @Inject constructor(private val context: Context) {
    fun createSharableUri(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName +
                    ".com.omar.retromp3recorder.app.provider",
            file
        )
    }
}
