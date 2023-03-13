package com.omar.retromp3recorder.app.screens.settings.components.components.checkbox_group

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
data class CheckboxGroupData(
    val title: String,
    val items: List<Item>
) {
    fun isNotEmpty() = items.isNotEmpty()


    data class Item(
        val title: String,
        val isSelected: Boolean
    )
}


class PreviewCheckboxGroupData : PreviewParameterProvider<CheckboxGroupData> {
    override val values = sequenceOf(
        CheckboxGroupData(
            "Settings group",
            listOf(
                CheckboxGroupData.Item("Option 1", true),
                CheckboxGroupData.Item("Option 2", false),
                CheckboxGroupData.Item("Option 4", true),
            ),
        )
    )
}
