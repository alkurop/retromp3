package com.omar.retromp3recorder.app.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class PreviewStringProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf(
        "Test string"
    )
}

class PreviewStringListProvider : PreviewParameterProvider<List<String>> {
    override val values = sequenceOf(
        listOf(
            "Option 1", "Option 2"
        )
    )
}
