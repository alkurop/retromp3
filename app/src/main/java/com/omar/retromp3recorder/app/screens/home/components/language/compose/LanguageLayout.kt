package com.omar.retromp3recorder.app.screens.home.components.language.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.RetroButtonDark
import com.omar.retromp3recorder.app.screens.home.components.Component
import com.omar.retromp3recorder.app.screens.home.components.language.LanguageContract
import com.omar.retromp3recorder.app.screens.home.components.language.LanguageViewModel
import com.omar.retromp3recorder.app.screens.home.components.rememberVisibilityState
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.domain.RecognitionLanguage

@Composable
fun LanguageLayout(
    modifier: Modifier = Modifier,
    viewModel: LanguageViewModel = hiltViewModel(),
    openLanguagePopup: () -> Unit = {}
) {
    val visibility = rememberVisibilityState(isVisible = false)
    val popupVisibility = rememberVisibilityState(isVisible = false)
    val viewState by viewModel.state.collectAsState()

    visibility.isVisible = viewState.isVisible

    val onLanguageSelected: (RecognitionLanguage) -> Unit = remember {
        {
            viewModel.onEvent(LanguageContract.Input.SelectLanguage(it))
        }
    }

    Component(modifier, visibility) {
        Column(Modifier.padding(8.dp)) {
            Row {
                Text(text = stringResource(id = R.string.language_recognition))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.language_disabled))
            }
            RetroButtonDark(
                onClick = { popupVisibility.switch() },
            ) { Text(text = stringResource(id = R.string.language_select)) }
            LanguageListPopup(
                state = popupVisibility,
                contentData = viewState.toPopupContent(),
                onAddLanguage = openLanguagePopup,
                onLanguageSelected = onLanguageSelected
            )
        }
    }
}

private fun LanguageContract.State.toPopupContent() = LanguagePopupData(
    this.selectedLanguage, this.availableLanguages
)

