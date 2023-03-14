package com.omar.retromp3recorder.data.mock

import com.omar.retromp3recorder.domain.NewNameSuggestion

object MockSuggestionFactory {
    fun giveTestSuggestion() = NewNameSuggestion(
        path = "test_path",
        name = "test_name"
    )
}
