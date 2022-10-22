package com.omar.retromp3recorder.app.ui.files.selector.jpc.layout

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
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
        Column(modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.surface)) {
            SearchToolbar(lambda)

            val itemsPaging = state.itemsPaging
            if (itemsPaging != null) {
                Results(data = itemsPaging) { query }
            }
        }
    }
}