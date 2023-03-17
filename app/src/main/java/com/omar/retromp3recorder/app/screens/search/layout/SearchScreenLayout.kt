package com.omar.retromp3recorder.app.screens.search.layout

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.RetroTheme
import com.omar.retromp3recorder.app.screens.search.SelectorContract
import com.omar.retromp3recorder.app.screens.search.SelectorViewModelFlow
import com.omar.retromp3recorder.domain.ExistingFileWrapper

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreenLayout(
    viewModel: SelectorViewModelFlow = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var query by remember { mutableStateOf("") }
    val currentFilePath = state.selectedFile
    val lambdaSearch: (String) -> Unit = remember { { query = it } }

    val lambdaClick: (ExistingFileWrapper) -> Unit = remember {
        {
            viewModel.onEvent(SelectorContract.Input.ItemSelected(it))
        }

    }
    if (state.shouldDismiss) {
        SideEffect {
            onBack()
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
