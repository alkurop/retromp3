package com.omar.retromp3recorder.app.ui.menu

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.viewmodel.compose.viewModel


@Preview
@Composable
fun MenuBarkComposable(viewModel: MenuViewModel = viewModel()) {
    Text(text = "item.text")
}


//private class PreviewMenuProvider : PreviewParameterProvider<MenuBarkView.MenuItem> {
//    override val values = sequenceOf(MenuBarkView.MenuItem("test"))
//}

