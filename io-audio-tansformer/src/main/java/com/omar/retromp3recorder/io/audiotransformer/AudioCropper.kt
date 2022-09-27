package com.omar.retromp3recorder.io.audiotransformer

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.omar.retromp3recorder.dto.FromToMillis
import javax.inject.Inject


class AudioCropper @Inject constructor(){
    fun crop(fromToMillis: FromToMillis, filePath: String, newFilePath: String): Boolean {
        val returnCode = FFmpeg.execute(
            "-ss ${fromToMillis.from}ms -to ${fromToMillis.to}ms -i $filePath-c:a copy $newFilePath"
        )
        return Config.RETURN_CODE_CANCEL != returnCode
    }
}
