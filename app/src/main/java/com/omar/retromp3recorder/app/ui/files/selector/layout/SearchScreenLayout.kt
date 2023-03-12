package com.omar.retromp3recorder.app.ui.files.selector.layout

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.ui.files.selector.SelectorContract
import com.omar.retromp3recorder.app.ui.files.selector.SelectorViewModel
import com.omar.retromp3recorder.app.ui.theme.RetroTheme
import com.omar.retromp3recorder.domain.ExistingFileWrapper

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreenLayout(viewModel: SelectorViewModel = viewModel()) {
    val state by viewModel.state.subscribeAsState(initial = SelectorContract.State())
    var query by remember { mutableStateOf("") }
    val currentFilePath = state.selectedFile
    val lambdaSearch: (String) -> Unit = remember { { query = it } }

    val onBackPressed: OnBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    val lambdaClick: (ExistingFileWrapper) -> Unit = remember {
        {
            viewModel.input.onNext(SelectorContract.Input.ItemSelected(it))
            onBackPressed.onBackPressed()
        }
    }

    RetroTheme {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            SearchToolbar(lambdaSearch)
            val itemsPaging = state.itemsPaging
            if (itemsPaging != null) {
                Results(
                    data = itemsPaging,
                    currentFilePath = currentFilePath,
                    query = query,
                    onClick = lambdaClick
                )
            }
        }
    }
}
