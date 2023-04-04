package com.omar.retromp3recorder.app.screens.search.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.RetroTheme
import com.omar.retromp3recorder.app.screens.search.SelectorContract
import com.omar.retromp3recorder.app.screens.search.SelectorViewModelFlow
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.utils.domain.LoadingState
import com.test.android.assignment.ui.searchbar.SearchBarLayout
import com.test.android.assignment.ui.searchbar.rememberSearchBarInputState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenLayout(
    viewModel: SelectorViewModelFlow = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val currentFilePath = state.selectedFile
    val lambdaSearch: (String) -> Unit = remember {
        {
            viewModel.onEvent(SelectorContract.Input.SetQuery(it))
        }
    }

    val lambdaClick: (ExistingFileWrapper) -> Unit = remember {
        {
            onBack()
            viewModel.onEvent(SelectorContract.Input.ItemSelected(it))
        }
    }
    val itemsPaging = state.flow as? LoadingState.Success
    val searchBarState =
        rememberSearchBarInputState(hint = stringResource(id = R.string.search_hint))

    RetroTheme {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            SearchBarLayout(
                onSubmit = lambdaSearch,
                state = searchBarState,
                onBack = onBack,
                backContentDescription = stringResource(id = R.string.back),
                clearContentDescription = stringResource(id = R.string.clear)
            ) {
                if (itemsPaging != null) {
                    Results(
                        data = itemsPaging.data,
                        currentFile = currentFilePath,
                        onClick = lambdaClick
                    )
                }
            }
        }
    }
}
