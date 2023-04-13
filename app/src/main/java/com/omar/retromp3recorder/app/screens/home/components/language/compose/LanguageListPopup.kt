package com.omar.retromp3recorder.app.screens.home.components.language.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.home.components.ComponentDivider
import com.omar.retromp3recorder.app.screens.home.components.VisibilityState
import com.omar.retromp3recorder.app.screens.home.components.language.getDisplayNameRes
import com.omar.retromp3recorder.domain.RecognitionLanguage

@Immutable
data class LanguagePopupData(
    val selectedLanguage: RecognitionLanguage? = null,
    val availableLanguages: List<RecognitionLanguage> = emptyList()
)

@Composable
fun LanguageListPopup(
    contentData: LanguagePopupData,
    state: VisibilityState,
    onLanguageSelected: (RecognitionLanguage) -> Unit,
    onAddLanguage: () -> Unit
) {
    val languages = contentData.availableLanguages
    val listState = rememberLazyListState()

    if (state.isVisible) {
        Popup(
            alignment = Alignment.CenterStart,
            onDismissRequest = { state.switch() },
        ) {
            LazyColumn(
                Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .width(200.dp)
                    .heightIn(max = 150.dp),
                state = listState,

                ) {

                items(languages.size, { languages[it] }) {
                    val language = languages[it]
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(language); state.switch() }
                            .padding(16.dp)
                    ) {
                        Text(text = stringResource(id = language.getDisplayNameRes()))
                        ComponentDivider()
                    }
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddLanguage(); state.switch() }
                            .padding(16.dp),
                        text = stringResource(R.string.popup_add_language)
                    )
                }
            }
        }
    }
}
