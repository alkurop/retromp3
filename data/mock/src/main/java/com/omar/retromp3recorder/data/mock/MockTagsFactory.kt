package com.omar.retromp3recorder.data.mock

import com.omar.retromp3recorder.domain.RecordingTags

object MockTagsFactory {
    fun giveTags() = RecordingTags(
        title = "tag title",
        artist = "tag artist",
        year = "tag year",
    )
}
