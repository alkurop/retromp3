package com.omar.retromp3recorder.app.screens.settings.components.selection_group

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider


@Immutable
data class SelectionGroupData(
    val title: String,
    val options: List<String>,
    val selection: Int
)

class PreviewSettingGroupData : PreviewParameterProvider<SelectionGroupData> {
    override val values = sequenceOf(
        SelectionGroupData(
            "Settings group",
            listOf(
                "Option 1", "Option 2"
            ), 1
        )
    )
}
