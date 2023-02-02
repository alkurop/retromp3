package com.omar.retromp3recorder.app.ui.menu.views.layout

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.omar.retromp3recorder.app.ui.menu.MenuContract
import com.omar.retromp3recorder.app.ui.theme.LocalSpacing

@Preview
@Composable
fun MenuLayout(
    modifier: Modifier = Modifier,
    @PreviewParameter(MenuPreviewStateProvider::class) state: MenuContract.State,
    onAction: (MenuContract.Input) -> Unit = {},
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
                onAction = if (state.isVisible) onAction else noAction
            )
        }
    }
}

@Composable
private fun getPadding(side: Boolean) = if (side) LocalSpacing.current.normal else LocalSpacing.current.x_small
