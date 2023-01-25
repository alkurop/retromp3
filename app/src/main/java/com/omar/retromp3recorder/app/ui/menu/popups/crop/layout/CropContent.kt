package com.omar.retromp3recorder.app.ui.menu.popups.crop.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omar.retromp3recorder.app.R

@Preview
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CropContent(
    title: String = "privet",
    maxChar: Int = 40,
    label: String = stringResource(id = R.string.popup_label_filename),
    isError: Boolean = false,
    onValueChanged: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf(TextFieldValue(title)) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 16.dp),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
        value = text,
        isError = isError,
        label = { Text(text = label) },
        keyboardActions = KeyboardActions(onAny = { keyboardController?.hide() }),
        onValueChange = {
            if (it.text.length <= maxChar) {
                text = it
                onValueChanged.invoke(it.text)
            }
        },
        singleLine = true,
    )
}
