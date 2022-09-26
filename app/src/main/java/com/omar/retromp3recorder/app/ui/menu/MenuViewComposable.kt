package com.omar.retromp3recorder.app.ui.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
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
        MenuPreviewStateProvider::class
    ) state: MenuView.State, onAction: (MenuAction) -> Unit = {}
) {
    if (state.isVisible.not())
        Spacer(modifier = Modifier)
    else
        Row() {
            state.items.map { DrawMenuItem(action = it, onAction = onAction) }
        }
}


@Composable
private fun DrawMenuItem(action: MenuAction, onAction: (MenuAction) -> Unit) = when (action) {
    is MenuAction.Execute -> DrawMenuExecutableItem(action, onAction)
    is MenuAction.Enable -> DrawEnablerItem(action, onAction)
}

@Composable
private fun DrawEnablerItem(action: MenuAction.Enable, onAction: (MenuAction) -> Unit) =
    when (action.enabler) {
        is AudioEnabler -> DrawAudioEnablerItem(action, onAction)
        is VisibilityEnabler -> DrawVisibilityEnablerItem(action, onAction)
    }


@Composable
private fun DrawAudioEnablerItem(action: MenuAction.Enable, onAction: (MenuAction) -> Unit) {
    Text(text = "$action", modifier = Modifier.clickable { onAction(action) })
}

@Composable
private fun DrawVisibilityEnablerItem(action: MenuAction.Enable, onAction: (MenuAction) -> Unit) {
    Text(text = "$action", modifier = Modifier.clickable { onAction(action) })
}

@Composable
private fun DrawMenuExecutableItem(action: MenuAction.Execute, onAction: (MenuAction) -> Unit) {
    Text(text = "text", Modifier.clickable { onAction(action) })
}






