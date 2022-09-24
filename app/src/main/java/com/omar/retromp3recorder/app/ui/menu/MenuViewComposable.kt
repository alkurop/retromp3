package com.omar.retromp3recorder.app.ui.menu

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.dto.AudioEnabler
import com.omar.retromp3recorder.dto.MenuAction
import com.omar.retromp3recorder.dto.VisibilityEnabler


@Composable
fun MenuViewComposable(viewModel: MenuViewModel = viewModel()) {
    val state: MenuView.State by viewModel.state
        .subscribeAsState(initial = MenuView.State())
    DrawMenu(state = state, onAction = { viewModel.input.onNext(it) })
}

@Preview
@Composable
private fun DrawMenu(
    @PreviewParameter(
        PreviewMenuStateProvider::class
    ) state: MenuView.State, onAction: (MenuAction) -> Unit = {
        println()
    }
) {
}

private class PreviewMenuStateProvider : PreviewParameterProvider<MenuView.State> {
    override val values = sequenceOf(
        MenuView.State(
            isVisible = true,
            items = listOf()
        )
    )
}

@Composable
private fun DrawMenuItem(action: MenuAction, onAction: (MenuAction) -> Unit) = when (action) {
    is MenuAction.Execute -> DrawMenuExecutableItem(action, onAction)
    is MenuAction.Enable -> DrawAudioEnablerItem(action, onAction)
}

@Composable
private fun DrawEnablerItem(action: MenuAction.Enable, onAction: (MenuAction) -> Unit) =
    when (action.enabler) {
        is AudioEnabler -> DrawAudioEnablerItem(action, onAction)
        is VisibilityEnabler -> DrawVisibilityEnablerItem(action, onAction)
    }


@Composable
private fun DrawAudioEnablerItem(action: MenuAction.Enable, onAction: (MenuAction) -> Unit) {
    Text(text = "$action")

}

@Composable
private fun DrawVisibilityEnablerItem(action: MenuAction.Enable, onAction: (MenuAction) -> Unit) {
    Text(text = "$action")
}

@Composable
private fun DrawMenuExecutableItem(action: MenuAction.Execute, onAction: (MenuAction) -> Unit) {
    Text(text = "$action")
}






