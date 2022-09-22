package com.omar.retromp3recorder.app.ui.menu

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider


@Preview
@Composable
fun MenuBarkComposable() {
    Text(text = "item.text")
}


private class PreviewMenuProvider : PreviewParameterProvider<MenuItem> {
    override val values = sequenceOf(MenuItem("test"))
}


data class MenuItem(
    val text: String
)

sealed class MenuAction {
    data class Execute(val menuExecutable: MenuExecutable) : MenuAction()
    data class Enable(val isEnabled: Boolean) : MenuAction()
// todo   object Navigate : MenuAction()
}

interface MenuExecutable

enum class AudioExecutable : MenuExecutable {
    Crop,
    Reverse,
    Loop
}

enum class FunctionEnabler {
    RangeBar,
    RangeBarZoom,
    PlaybackSpeed
}
