package com.omar.retromp3recorder.utils.platform

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class FileUriCreator @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun createSharableUri(file: File): Uri {
        return FileProvider.getUriForFile(
            context, "${context.applicationContext.packageName}.provider", file
        )
    }
}
