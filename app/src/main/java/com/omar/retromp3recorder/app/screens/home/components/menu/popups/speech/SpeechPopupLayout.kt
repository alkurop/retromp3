package com.omar.retromp3recorder.app.screens.home.components.menu.popups.speech

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.home.components.ComponentDivider
import com.omar.retromp3recorder.app.screens.home.components.language.getDisplayNameRes
import com.omar.retromp3recorder.app.screens.home.components.menu.views.PopupComposable

@Composable
fun SpeechPopupLayout(
    viewModel: SpeechPopupViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val listState = rememberLazyListState()
    val state by viewModel.state.collectAsState()
    val languages = state.languages

    PopupComposable(
        title = stringResource(id = R.string.popup_title_language),
        buttonList = listOf(),
        maxHeight = 500.dp,
        contentPaddingValues = PaddingValues(vertical = 8.dp),
        content = {
            LazyColumn(
                state = listState,
            ) {
                items(languages.size, { languages[it].language }) {
                    val language = languages[it]
                    Column(
                        Modifier
                            .fillMaxWidth()

                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(id = language.language.getDisplayNameRes())
                        )
                        ComponentDivider()
                    }
                }
            }
        },
        dismissButtonRes = R.string.popup_close,
        onDismiss = onDismiss,
    )
}

