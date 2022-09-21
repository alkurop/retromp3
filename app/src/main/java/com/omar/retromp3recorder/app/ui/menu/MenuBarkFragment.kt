package com.omar.retromp3recorder.app.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.fragment.app.Fragment

class MenuBarkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Text(text = "Hello world.")
            }
        }
    }

    @Preview
    @Composable
    fun MenuItem(@PreviewParameter(PreviewMenuProvider::class) item: MenuItem) {
        Text(text = item.text)
    }

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
