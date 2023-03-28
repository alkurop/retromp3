package com.test.android.assignment.ui.searchbar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp


interface SearchInputLayout {
    data class Props(
        val hint: String,
        val query: TextFieldValue
    )
}


/**
 * Input field, that encapsulates
 * UI logic for search.
 *
 * @param onDone Callback when user clicks Done button on the Soft Keyboard
 * @param onValueChange Callback when changes input text
 * @param onClear Callback when user clicks Clear button
 */


@Preview
@Composable
fun SearchInputLayout(
    @PreviewParameter(SearchParamsPreviewProvider::class) props: SearchInputLayout.Props,
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit = {},
    onDone: KeyboardActionScope.() -> Unit = {},
    onClear: () -> Unit = {},
    clearContentDescription: String? = null
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 8.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val isQueryEmpty = props.query.text.isEmpty()
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .weight(1f)
            ) {
                // Search box
                BasicTextField(
                    value = props.query,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSecondary),
                    onValueChange = {
                        onValueChange(it.copy(it.text.trim()))
                    },
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondary),
                    keyboardActions = KeyboardActions(onDone = onDone),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Uri,
                        autoCorrect = false
                    ),
                )
                // Hint
                if (isQueryEmpty) {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        text = props.hint,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            //Clear button
            if (isQueryEmpty.not()) {
                IconButton(
                    onClick = onClear
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = clearContentDescription
                    )
                }
            }
        }
    }
}
