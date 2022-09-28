package com.omar.retromp3recorder.app.ui.menu.popups

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.omar.retromp3recorder.app.ui.menu.MenuView
import com.omar.retromp3recorder.app.ui.menu.MenuViewModel

@Composable
fun CropPopup(viewModel: MenuViewModel = viewModel()) {
    AppCompatTheme() {
        Dialog(onDismissRequest = {

        }, content = {
            Text(text = "hello")
        })
    }
}
