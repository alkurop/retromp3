package com.omar.retromp3recorder.io.audiotransformer

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.FromToMillis
import com.omar.retromp3recorder.dto.NewNameSuggestion
import javax.inject.Inject


class AudioCropper @Inject constructor() {
    fun crop(request: CropRequest): CropResponse {
        val returnCode = FFmpeg.execute(
            "-ss ${request.range.from}ms " + "-to ${request.range.to}ms -i " + "${request.existingFileWrapper.path} -c:a " + "copy $${request.nameSuggestion.path}"
        )
        return CropResponse(Config.RETURN_CODE_CANCEL != returnCode)
    }
}

data class CropRequest(
    val range: FromToMillis,
    val existingFileWrapper: ExistingFileWrapper,
    val nameSuggestion: NewNameSuggestion
)

data class CropResponse(
    val isSuccess: Boolean
)