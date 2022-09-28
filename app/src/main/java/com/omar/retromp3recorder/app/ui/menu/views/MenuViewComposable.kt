package com.omar.retromp3recorder.app.ui.menu.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.ui.menu.MenuPreviewStateProvider
import com.omar.retromp3recorder.app.ui.menu.logic.MenuView
import com.omar.retromp3recorder.app.ui.menu.logic.MenuViewModel
import com.omar.retromp3recorder.app.ui.menu.logic.toInput
import com.omar.retromp3recorder.app.ui.menu.popups.MenuPopups


@Composable
fun MenuViewComposable(viewModel: MenuViewModel = viewModel()) {
    val state: MenuView.State by viewModel.state
        .subscribeAsState(initial = MenuView.State())
    DrawMenu(state = state, onAction = {
        viewModel.input.onNext(it)
    })

}

@Preview
@Composable
private fun DrawMenu(
    @PreviewParameter(
        MenuPreviewStateProvider::class
    ) state: MenuView.State, onAction: (MenuView.Input) -> Unit = {}
) {
    Spacer(modifier = Modifier)
    if (state.isVisible.not()) {
        Spacer(modifier = Modifier)
    } else {
        val scrollState = rememberScrollState()
        Row(
            Modifier
                .horizontalScroll(scrollState)
        ) {
            state.items.mapIndexed { index, item ->
                val isFirst = index == 0
                val isLast = index == state.items.size - 1
                DrawMenuItem(
                    Modifier.padding(start = getPadding(isFirst), end = getPadding(isLast)),
                    action = item,
                    onAction = onAction
                )

                state.popup.ghost?.let {
                    MenuPopups.showPopup(it)
                }
            }
        }
    }
}

private fun getPadding(side: Boolean) = if (side) 16.dp else 4.dp

@Composable
private fun DrawMenuItem(
    modifier: Modifier,
    action: MenuView.Item,
    onAction: (MenuView.Input) -> Unit
) =
    when (action) {
        is MenuView.Item.Popup -> DrawMenuPopupItem(modifier, action, onAction)
        is MenuView.Item.Enable -> DrawEnablerItem(modifier, action, onAction)
    }

@Composable
private fun DrawEnablerItem(
    modifier: Modifier,
    item: MenuView.Item.Enable,
    onAction: (MenuView.Input) -> Unit
) {
    MenuItemComposable(
        modifier = modifier.clickable {
            onAction(item.toInput())
        },
        title = stringResource(
            id = item.enabler.getTitleRes()
        ),
        state = item.isEnabled.mapToStateEnabler()
    )
}

@Composable
private fun DrawMenuPopupItem(
    modifier: Modifier,
    item: MenuView.Item.Popup,
    onAction: (MenuView.Input) -> Unit
) {
    MenuItemComposable(
        modifier = modifier.clickable(item.isEnabled) { onAction.invoke(item.toInput()) },
        title = stringResource(
            id = item.menuExecutable.getTitleRes()
        ),
        state = item.isEnabled.mapToStatePopup()
    )
}
