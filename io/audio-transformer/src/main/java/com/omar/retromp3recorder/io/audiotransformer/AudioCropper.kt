package com.omar.retromp3recorder.io.audiotransformer

import com.omar.retromp3recorder.domain.CropRequest
import com.omar.retromp3recorder.utils.domain.toResult
import com.omar.retromp3recorder.utils.platform.FileLister
import javax.inject.Inject


class AudioCropper @Inject constructor(
    private val fileLister: FileLister
) {
    fun crop(request: CropRequest): Result<Unit> {
         return CropError.toResult()
    }
}

object CropError : Throwable()
