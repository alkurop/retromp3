package com.omar.retromp3recorder.io.audiotransformer

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.omar.retromp3recorder.dto.FromToMillis
import com.omar.retromp3recorder.dto.toSecondsEnd
import com.omar.retromp3recorder.dto.toSecondsStart
import javax.inject.Inject


class AudioCropper @Inject constructor(){
    fun crop(fromToMillis: FromToMillis, filePath: String, newFilePath: String): Boolean {
        val returnCode = FFmpeg.execute(
            "-ss ${fromToMillis.from.toSecondsStart()} -to ${fromToMillis.to.toSecondsEnd()} -i $filePath-c:a copy $newFilePath"
        )
        return Config.RETURN_CODE_CANCEL != returnCode
    }
}