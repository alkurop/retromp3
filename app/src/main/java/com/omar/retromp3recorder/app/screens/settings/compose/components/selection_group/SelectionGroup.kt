package com.omar.retromp3recorder.app.screens.settings.compose.components.selection_group

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.omar.retromp3recorder.app.RetroTheme

@Composable
fun SelectionGroup(
    settings: SelectionGroupData,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit = {},
) {
    Column(modifier) {

        Text(
            text = settings.title,
            style = MaterialTheme.typography.labelLarge
        )
        settings.options.forEachIndexed { index, text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .padding(top = 4.dp)
                    .selectable(selected = (index == settings.selection), onClick = {
                        onSelected(index)
                    })
            ) {
                RadioButton(
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary),
                    selected = (index == settings.selection),
                    onClick = { onSelected(index) })
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(start = 16.dp)

                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingGroup(
    @PreviewParameter(PreviewSettingGroupData::class) settings: SelectionGroupData,
) {
    RetroTheme {
        SelectionGroup(settings = settings)
    }
}
