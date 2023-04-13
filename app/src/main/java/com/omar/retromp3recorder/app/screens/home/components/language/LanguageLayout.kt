package com.omar.retromp3recorder.app.screens.home.components.language

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.screens.home.components.Component
import com.omar.retromp3recorder.app.screens.home.components.rememberComponentState

@Composable
fun LanguageLayout(
    modifier: Modifier = Modifier,
    viewModel: LanguageViewModel = hiltViewModel()
) {
    val visibility = rememberComponentState(isVisible = false)
    val viewState by viewModel.state.collectAsState()
    visibility.isVisible = viewState.isVisible

    Component(modifier, visibility) {
        Box(modifier = Modifier.height(400.dp))
    }
}
