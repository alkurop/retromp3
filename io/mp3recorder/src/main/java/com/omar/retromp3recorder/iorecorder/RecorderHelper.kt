package com.omar.retromp3recorder.iorecorder

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioPlaybackCaptureConfiguration
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("MissingPermission")
internal fun findAudioRecordForMediaProjection(
    mediaProjection: MediaProjection,
    source: Int,
    minBufferSize: Int,
    sampleRate: Int,
): Result<AudioRecord> {
    val config = AudioPlaybackCaptureConfiguration.Builder(mediaProjection)
        .addMatchingUsage(source)
        .build()

    val audioFormat = AudioFormat.Builder()
        .setEncoding(Mp3VoiceRecorderLame.encoding)
        .setSampleRate(sampleRate)
        .setChannelMask(Mp3VoiceRecorderLame.channelConfig)
        .build()

    return Result.success(
        AudioRecord.Builder()
            .setBufferSizeInBytes(minBufferSize * Mp3VoiceRecorderLame.MIN_BUFFER_SIZE_INDEX)
            .setAudioFormat(audioFormat)
            .setAudioPlaybackCaptureConfig(config)
            .build()
    )
}

@SuppressLint("MissingPermission")
@Throws(Exception::class)
internal fun findAudioRecordForMic(
    minBufferSize: Int,
    sampleRate: Int,
): Result<AudioRecord> {
    return if (minBufferSize != AudioRecord.ERROR_BAD_VALUE) {
        Result.success(
            AudioRecord(
                MediaRecorder.AudioSource.DEFAULT,
                sampleRate,
                Mp3VoiceRecorderLame.channelConfig,
                Mp3VoiceRecorderLame.encoding,
                minBufferSize * Mp3VoiceRecorderLame.MIN_BUFFER_SIZE_INDEX
            )
        )
    } else {
        Result.failure(Error("AudioRecord.ERROR_BAD_VALUE"))
    }
}

internal fun createMp3Buffer(buffer: ShortArray): ByteArray {
    val mp3BufferSize = 7200 + buffer.size * 1.25
    return ByteArray(mp3BufferSize.toInt())
}

internal fun createOutputFile(filePath: String): Result<File> {
    val outFile = File(filePath)
    if (outFile.exists()) {
        outFile.delete()
    }
    val fileWasCreated = outFile.createNewFile()
    return if (!fileWasCreated) {
        Result.failure(IOException("File $filePath was not created"))
    } else {
        Result.success(outFile)
    }
}

