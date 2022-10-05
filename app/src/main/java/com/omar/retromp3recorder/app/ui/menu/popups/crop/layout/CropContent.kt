package com.omar.retromp3recorder.app.ui.menu.popups.crop.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omar.retromp3recorder.app.R

@Preview
@OptIn(ExperimentalComposeUiApi::class)
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