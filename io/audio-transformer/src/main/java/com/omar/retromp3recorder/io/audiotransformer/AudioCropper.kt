package com.omar.retromp3recorder.io.audiotransformer

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.omar.retromp3recorder.domain.CropRequest
import com.omar.retromp3recorder.utils.domain.toResult
import com.omar.retromp3recorder.utils.platform.FileLister
import timber.log.Timber
import java.io.File
import javax.inject.Inject


class AudioCropper @Inject constructor(
    private val fileLister: FileLister
) {
    fun crop(request: CropRequest): Result<Unit> {
        val path = request.newFileNameSuggestion.path
        val command =
            "-ss ${request.range.from}ms -to ${request.range.to}ms -i " + "${request.original.path} -c:a  copy $path"
        val returnCode = FFmpeg.execute(
            command
        )
        val isCropSuccess = Config.RETURN_CODE_SUCCESS == returnCode
        val isFileNotEmpty = fileLister.discoverLength(path) > 0L

        val isSuccess = isCropSuccess && isFileNotEmpty
        return if (!isSuccess) {
            try {
                File(path).takeIf { it.exists() }?.delete()
            } catch (e: Throwable) {
                Timber.e(e)
            }
            CropError.toResult()
        } else {
            Unit.toResult()
        }
    }
}

object CropError : Throwable()
