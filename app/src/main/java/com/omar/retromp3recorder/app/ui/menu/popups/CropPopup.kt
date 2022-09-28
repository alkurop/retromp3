package com.omar.retromp3recorder.app.ui.menu.popups

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.omar.retromp3recorder.app.ui.menu.logic.MenuView
import com.omar.retromp3recorder.app.ui.menu.logic.MenuViewModel

@Composable
fun CropPopup(viewModel: MenuViewModel = viewModel()) {
    AppCompatTheme() {
        Dialog(onDismissRequest = {
            viewModel.input.onNext(MenuView.Input.DismissPopup)
        }, content = {
            Text(text = "hello")
        })
    }
}

@Preview
@Composable
fun PopupComposable(
    title: String = "Hello",
    buttons: List<@Composable RowScope.() -> Unit> = emptyList()
) {
    AppCompatTheme() {
        Dialog(onDismissRequest = {

        },
            content = {
                Text(text = "hello")
            })
    }
}

@Composable
fun PopupButton(
    text: String,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(onClick = onClick, enabled = isEnabled) {
        Text(text = text)
    }
}



