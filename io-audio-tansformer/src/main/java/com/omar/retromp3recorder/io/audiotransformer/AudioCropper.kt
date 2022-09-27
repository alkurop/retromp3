package com.omar.retromp3recorder.io.audiotransformer

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.omar.retromp3recorder.dto.FromToMillis
import javax.inject.Inject


class AudioCropper @Inject constructor() {
    fun crop(request: CropRequest): Boolean {
        val returnCode = FFmpeg.execute(
            "-ss ${request.fromToMillis.from}ms -to ${request.fromToMillis.to}ms -i ${request.originalFilePath} -c:a copy $${request.newFilePath}"
        )
        return Config.RETURN_CODE_CANCEL != returnCode
    }
}

data class CropRequest(
    val fromToMillis: FromToMillis,
    val originalFilePath: String,
    val newFilePath: String
)
