package com.test.android.assignment.ui.searchbar

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class SearchParamsPreviewProvider : PreviewParameterProvider<SearchInputLayout.Props> {
    override val values = sequenceOf(
        SearchInputLayout.Props("Test Initial text", TextFieldValue("Hint")),
        SearchInputLayout.Props("", TextFieldValue("Hint")),
    )
}
