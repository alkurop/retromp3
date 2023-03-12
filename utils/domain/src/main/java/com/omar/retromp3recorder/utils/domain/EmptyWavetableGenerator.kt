package com.omar.retromp3recorder.utils.domain

import android.media.MediaMetadataRetriever
import com.omar.retromp3recorder.domain.Wavetable
import javax.inject.Inject

class EmptyWavetableGenerator @Inject constructor(
    private val fileEmptyChecker: FileEmptyChecker,
) {
    fun generateWavetable(filepath: String, waveTableSampleRate: Int): Wavetable {
        return if (fileEmptyChecker.isFileEmpty(filepath).not()) {
            val metaRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filepath)
            val duration =
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!
                    .toLong()
            val data =
                (0 until duration.toSeekbarTime()).map { 0 }.map { it.toByte() }.toByteArray()
            Wavetable(data, waveTableSampleRate)
        } else {
            Wavetable(ByteArray(0), waveTableSampleRate)
        }
    }
}
