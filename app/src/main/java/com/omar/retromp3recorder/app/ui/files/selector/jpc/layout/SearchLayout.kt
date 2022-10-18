package com.omar.retromp3recorder.app.ui.files.selector.jpc.layout

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.omar.retromp3recorder.app.ui.RetroTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchLayout() {
    RetroTheme {
        Column {
            SearchToolbar()
            Results()
        }
    }
}