package com.omar.retromp3recorder.app.ui.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.dto.MenuAction


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
    if (state.isVisible.not()) {
        Spacer(modifier = Modifier)
    } else {
        val scrollState = rememberScrollState()
        Row(Modifier.horizontalScroll(scrollState)) {
            state.items.map { DrawMenuItem(action = it, onAction = onAction) }
        }
    }
}


@Composable
private fun DrawMenuItem(action: MenuAction, onAction: (MenuAction) -> Unit) = when (action) {
    is MenuAction.Execute -> DrawMenuExecutableItem(action, onAction)
    is MenuAction.Enable -> DrawEnablerItem(action, onAction)
}

@Composable
private fun DrawEnablerItem(action: MenuAction.Enable, onAction: (MenuAction) -> Unit) {
    MenuItemComposable(
        modifier = Modifier.clickable { onAction(action) },
        title = stringResource(
            id = action.enabler.getTitleRes()
        ),
        state = action.isEnabled.mapToState()
    )
}

@Composable
private fun DrawMenuExecutableItem(action: MenuAction.Execute, onAction: (MenuAction) -> Unit) {
    MenuItemComposable(
        modifier = Modifier.clickable { onAction(action) },
        title = stringResource(
            id = action.menuExecutable.getTitleRes()
        ),
        state = MenuItemState.OneTime
    )
}
