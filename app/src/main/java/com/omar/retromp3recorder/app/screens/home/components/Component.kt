package com.omar.retromp3recorder.app.screens.home.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


@Composable
fun Component(
    modifier: Modifier = Modifier,
    state: ComponentState = rememberComponentState(isVisible = true),
    view: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = state.isVisible,
        enter = expandIn { fullSize -> IntSize(fullSize.width, 0) },
        exit = shrinkOut { fullSize -> IntSize(fullSize.width, 0) },
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            view()
            ComponentDivider()
        }
    }
}

@Composable
private fun ComponentDivider() {
    Divider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    )
}

@Stable
class ComponentState(isVisible: Boolean) {
    var isVisible by mutableStateOf(isVisible)

    companion object {
        val Saver: Saver<ComponentState, Any> = listSaver(
            save = {
                listOf(
                    it.isVisible
                )
            },
            restore = {
                val isVisible = it[0]
                ComponentState(isVisible)
            })
    }
}

@Composable
fun rememberComponentState(isVisible: Boolean): ComponentState =
    rememberSaveable(saver = ComponentState.Saver) {
        ComponentState(isVisible)
    }

