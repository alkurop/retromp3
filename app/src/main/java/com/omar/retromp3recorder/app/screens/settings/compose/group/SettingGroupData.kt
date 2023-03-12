package com.omar.retromp3recorder.app.screens.settings.compose.group

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider


@Immutable
data class SettingGroupData(
    val title: String,
    val options: List<String>,
    val selection: Int
)

class PreviewSettingGroupData : PreviewParameterProvider<SettingGroupData> {
    override val values = sequenceOf(
        SettingGroupData(
            "Settings group",
            listOf(
                "Option 1", "Option 2"
            ), 1
        )
    )
}
