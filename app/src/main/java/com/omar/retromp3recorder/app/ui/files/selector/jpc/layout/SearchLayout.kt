package com.omar.retromp3recorder.app.ui.files.selector.jpc.layout

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.ui.RetroTheme
import com.omar.retromp3recorder.app.ui.files.selector.SelectorContract
import com.omar.retromp3recorder.app.ui.files.selector.SelectorViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchLayout(viewModel: SelectorViewModel = viewModel()) {
    val state by viewModel.state.subscribeAsState(initial = SelectorContract.State())
    var query by remember { mutableStateOf("") }

    val lambda: (String) -> Unit = remember {
        {
            query = it
        }
    }
    RetroTheme {
        Column {
            SearchToolbar(lambda)

            val itemsPaging = state.itemsPaging
            if (itemsPaging == null) {
                Text(
                    text = "Empty",
                    color = MaterialTheme.colors.primary,
                )
            } else {
                Results(data = itemsPaging) { query }
            }
        }
    }
}