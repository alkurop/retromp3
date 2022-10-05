package com.omar.retromp3recorder.app.ui.menu.container.views.layout

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.omar.retromp3recorder.app.ui.menu.container.logic.MenuView
import com.omar.retromp3recorder.app.ui.menu.container.logic.toInput
import com.omar.retromp3recorder.app.ui.menu.container.views.MenuItemComposable
import com.omar.retromp3recorder.app.ui.menu.container.views.getTitleRes
import com.omar.retromp3recorder.app.ui.menu.container.views.mapToStateEnabler
import com.omar.retromp3recorder.app.ui.menu.container.views.mapToStatePopup

@Composable
fun DrawMenuItem(
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