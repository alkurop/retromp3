package com.omar.retromp3recorder.app.screens.home.components.language.popup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.RetroButtonDark
import com.omar.retromp3recorder.app.RetroProgressIndicator
import com.omar.retromp3recorder.app.screens.home.components.ComponentDivider
import com.omar.retromp3recorder.app.screens.home.components.language.getDisplayNameRes
import com.omar.retromp3recorder.app.screens.home.components.menu.views.PopupComposable
import com.omar.retromp3recorder.domain.LanguageState
import com.omar.retromp3recorder.domain.RecognitionLanguage

@Composable
fun SpeechPopupLayout(
    viewModel: SpeechPopupViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val listState = rememberLazyListState()
    val state by viewModel.state.collectAsState()
    val languages = state.languages

    val downloadFunction: (RecognitionLanguage) -> Unit = remember {
        {
            viewModel.emit(SpeechPopupContract.Input.DownloadLanguage(it))
        }
    }

    val deleteFunction: (RecognitionLanguage) -> Unit = remember {
        {
            viewModel.emit(SpeechPopupContract.Input.DeleteLanguage(it))
        }
    }

    val cancelFunction: (RecognitionLanguage) -> Unit = remember {
        {
            viewModel.emit(SpeechPopupContract.Input.CancelDownload(it))
        }
    }

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
                    val item = languages[it]
                    Column(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Row() {
                            when (val loadingState = item.state) {
                                is LanguageState.ToDownload -> DownloadLanguage(
                                    Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                    language = item.language,
                                    onDownload = downloadFunction,
                                    isFailed = loadingState.isFailed
                                )
                                LanguageState.Available -> AvailableLanguage(
                                    Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                    language = item.language,
                                    onDelete = deleteFunction
                                )
                                is LanguageState.Loading -> LoadingLanguage(
                                    Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                    language = item.language,
                                    onCancelLoad = cancelFunction,
                                    progress = "${loadingState.percent}%"
                                )
                            }
                        }
                        ComponentDivider()
                    }
                }
            }
        },
        dismissButtonRes = R.string.popup_close,
        onDismiss = onDismiss,
    )
}

@Composable
private fun LoadingLanguage(
    modifier: Modifier = Modifier,
    language: RecognitionLanguage,
    progress: String,
    onCancelLoad: (RecognitionLanguage) -> Unit
) {
    Row(modifier) {
        LanguageDisplay(
            language = language,
            Modifier
                .weight(1f)
                .align(CenterVertically)
        )
        RetroProgressIndicator(
            Modifier
                .size(28.dp)
                .align(CenterVertically)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = progress)
        RetroButtonDark(onClick = { onCancelLoad.invoke(language) }) {
            Text(text = stringResource(R.string.language_cancel_download))
        }
    }
}

@Composable
private fun AvailableLanguage(
    modifier: Modifier = Modifier,
    language: RecognitionLanguage,
    onDelete: (RecognitionLanguage) -> Unit
) {
    Row(modifier) {
        LanguageDisplay(
            language = language,
            Modifier
                .weight(1f)
                .align(CenterVertically)
        )
        RetroButtonDark(onClick = { onDelete(language) }) {
            Text(text = stringResource(R.string.language_delete))
        }
    }
}

@Composable
private fun DownloadLanguage(
    modifier: Modifier = Modifier,
    language: RecognitionLanguage,
    isFailed: Boolean,
    onDownload: (RecognitionLanguage) -> Unit
) {
    Row(modifier) {
        LanguageDisplay(
            language = language,
            Modifier
                .weight(1f)
                .align(CenterVertically)
        )
        if (isFailed) {
            Text(text = "Failed")
        }
        RetroButtonDark(onClick = { onDownload.invoke(language) }) {
            Text(text = stringResource(R.string.language_download))
        }
    }
}

@Composable
private fun LanguageDisplay(language: RecognitionLanguage, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(id = language.getDisplayNameRes())
    )
}

