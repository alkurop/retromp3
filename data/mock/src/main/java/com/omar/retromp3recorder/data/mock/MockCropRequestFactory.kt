package com.omar.retromp3recorder.data.mock

import com.omar.retromp3recorder.domain.CropRequest
import com.omar.retromp3recorder.domain.FromToMillis

object MockCropRequestFactory {
    fun giveCropRequest() = CropRequest(
        range = FromToMillis(10, 20),
        original = MockFileFactory.giveFile(),
        newFileNameSuggestion = MockSuggestionFactory.giveTestSuggestion()
    )
}
