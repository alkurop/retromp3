package com.omar.retromp3recorder.io.audiotransformer

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.FromToMillis
import com.omar.retromp3recorder.dto.NewNameSuggestion
import javax.inject.Inject


class AudioCropper @Inject constructor() {
    fun crop(request: CropRequest): CropResponse {
        val command =
            "-ss ${request.range.from}ms -to ${request.range.to}ms -i " + "${request.original.path} -c:a  copy ${request.newFileNameSuggestion.path}"
        val returnCode = FFmpeg.execute(
            command
        )
        val cropResponse = CropResponse(Config.RETURN_CODE_SUCCESS == returnCode)
        return cropResponse
    }
}

data class CropRequest(
    val range: FromToMillis,
    val original: ExistingFileWrapper,
    val newFileNameSuggestion: NewNameSuggestion
)

data class CropResponse(
    val isSuccess: Boolean
)