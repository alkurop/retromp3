package com.omar.retromp3recorder.app.screens.settings.components.checkbox_group

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.omar.retromp3recorder.app.RetroTheme

@Composable
fun CheckboxGroup(
    settings: CheckboxGroupData,
    onItemSelected: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (settings.isNotEmpty()) {
        Column(modifier) {
            Text(
                text = settings.title,
                style = MaterialTheme.typography.labelLarge
            )
            settings.items.forEachIndexed { index, item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .padding(top = 4.dp)
                        .clickable {
                            onItemSelected(index, item.isSelected.not())
                        }
                ) {
                    Checkbox(
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.secondary),
                        onCheckedChange = { isChecked -> onItemSelected(index, isChecked) },
                        checked = item.isSelected
                    )
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 16.dp)

                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewCheckboxGroup(
    @PreviewParameter(PreviewCheckboxGroupData::class) settings: CheckboxGroupData,
) {
    RetroTheme {
        CheckboxGroup(settings = settings, onItemSelected = { _, _ -> })
    }
}
