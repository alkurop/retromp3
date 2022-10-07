package com.omar.retromp3recorder.app.ui.files.selector.jpc.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun Toolbar(
    onBackPressed: () -> Unit = {},
    onSearch: (String) -> Unit = {}
) {
    val input = remember { mutableListOf<String>() }

}