package com.omar.retromp3recorder.io.audiotransformer

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.omar.retromp3recorder.domain.CropRequest
import com.omar.retromp3recorder.domain.CropResponse
import com.omar.retromp3recorder.utils.platform.FileLister
import javax.inject.Inject


class AudioCropper @Inject constructor(
    private val fileLister: FileLister
) {
    fun crop(request: CropRequest): CropResponse {
        val command =
            "-ss ${request.range.from}ms -to ${request.range.to}ms -i " + "${request.original.path} -c:a  copy ${request.newFileNameSuggestion.path}"
        val returnCode = FFmpeg.execute(
            command
        )
        val isCropSuccess = Config.RETURN_CODE_SUCCESS == returnCode
        val isFileNotEmpty = fileLister.discoverLength(request.newFileNameSuggestion.path) > 0L
        return CropResponse(isCropSuccess && isFileNotEmpty)
    }
}

