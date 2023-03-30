package com.omar.retromp3recorder.app.screens.main.components.menu.views.layout

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.main.components.menu.getTitleRes
import com.omar.retromp3recorder.app.screens.main.components.menu.toInput
import com.omar.retromp3recorder.app.screens.main.components.menu.views.EnablerMenuItem
import com.omar.retromp3recorder.app.screens.main.components.menu.views.PopupMenuItem
import com.omar.retromp3recorder.domain.MenuPopup

@Composable
fun DrawMenuItem(
    modifier: Modifier,
    action: MenuContract.Item,
    onAction: (MenuContract.Input) -> Unit,
    onNavigation: (MenuPopup) -> Unit
) =
    when (action) {
        is MenuContract.Item.Popup -> DrawMenuPopupItem(modifier, action, onNavigation)
        is MenuContract.Item.Enable -> DrawEnablerItem(modifier, action, onAction)
    }

@Composable
private fun DrawEnablerItem(
    modifier: Modifier,
    item: MenuContract.Item.Enable,
    onAction: (MenuContract.Input) -> Unit
) {
    EnablerMenuItem(
        modifier = modifier.clickable {
            onAction(item.toInput())
        },
        title = stringResource(
            id = item.enabler.getTitleRes()
        ),
        isEnabled = item.isActive,
        isOpen = item.isOpen,
    )
}

@Composable
private fun DrawMenuPopupItem(
    modifier: Modifier,
    item: MenuContract.Item.Popup,
    onOpenPopup: (MenuPopup) -> Unit
) {
    PopupMenuItem(
        modifier = modifier.clickable(item.isEnabled) {
            onOpenPopup.invoke(item.menuPopup)
        },
        title = stringResource(
            id = item.menuPopup.getTitleRes()
        ),
        isEnabled = item.isEnabled
    )
}
