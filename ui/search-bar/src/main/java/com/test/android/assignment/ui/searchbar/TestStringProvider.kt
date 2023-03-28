package com.test.android.assignment.ui.searchbar

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class TestStringProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf(
        "Test string"
    )
}
