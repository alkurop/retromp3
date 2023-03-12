package com.omar.retromp3recorder.app.screens.main.components.menu.views.layout

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.domain.MenuPopup

@Preview
@Composable
fun MenuLayout(
    @PreviewParameter(MenuPreviewStateProvider::class) state: MenuContract.State,
    modifier: Modifier = Modifier,
    onAction: (MenuContract.Input) -> Unit = {},
    onNavigation: (MenuPopup) -> Unit = {},
) {
    val noAction: (MenuContract.Input) -> Unit = {}
    val scrollState = rememberScrollState()
    Row(
        modifier
            .alpha(if (state.isVisible) 1f else 0.5f)
            .horizontalScroll(scrollState)
    ) {
        state.items.mapIndexed { index, item ->
            val isFirst = index == 0
            val isLast = index == state.items.size - 1
            DrawMenuItem(
                Modifier.padding(start = getPadding(isFirst), end = getPadding(isLast)),
                action = item,
                onAction = if (state.isVisible) onAction else noAction,
                onNavigation = onNavigation
            )
        }
    }
}

@Composable
private fun getPadding(side: Boolean) = if (side) 16.dp else 4.dp
