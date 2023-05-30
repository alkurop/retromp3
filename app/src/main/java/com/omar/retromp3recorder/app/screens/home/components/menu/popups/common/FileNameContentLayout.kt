package com.omar.retromp3recorder.app.screens.home.components.menu.popups.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.omar.retromp3recorder.app.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FileNameContentLayout(
    state: FileNameContentData = rememberFileNameInputState(""),
    maxChar: Int = 40,
    label: String = stringResource(id = R.string.popup_label_filename),
    isError: Boolean = false,
    onValueChanged: (String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 24.dp),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
        value = state.text,
        isError = isError,
        label = { Text(text = label) },
        keyboardActions = KeyboardActions(onAny = { keyboardController?.hide() }),
        onValueChange = {
            if (it.text.length <= maxChar) {
                state.text = it
                onValueChanged.invoke(it.text)
            }
        },
        singleLine = true,
    )
}

@Stable
class FileNameContentData(initialName: TextFieldValue) {
    constructor(initialName: String) : this(TextFieldValue(initialName))

    var text by mutableStateOf(initialName)

    companion object {
        val Saver: Saver<FileNameContentData, Any> = listSaver(
            save = {
                listOf(
                    TextFieldValue.Saver.run { save(it.text) },
                )
            },
            restore = {
                val input = TextFieldValue.Saver.restore(it[0]!!)!!
                FileNameContentData(input)
            })
    }
}


@Composable
fun rememberFileNameInputState(name: String): FileNameContentData =
    rememberSaveable(name, saver = FileNameContentData.Saver) {
        FileNameContentData(name)
    }
