package com.omar.retromp3recorder.app.ui.files.selector.jpc.layout

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.ui.files.selector.SelectorView
import com.omar.retromp3recorder.app.ui.files.selector.SelectorViewModel


@Composable
fun Results(
    searchQuery: () -> String = { "Hello" },
    viewModel: SelectorViewModel = viewModel()
) {
    val state by viewModel.state.subscribeAsState(initial = SelectorView.State())

    Text(
        text = searchQuery.invoke(),
        color = MaterialTheme.colors.primary,
    )
}