package com.test.android.assignment.ui.searchbar

import androidx.compose.animation.*
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * AppBar with Search field, Suggestions.
 *
 * @param state - Encapsulated inout state
 * @param onSubmit - Callback, that will be triggered
 * once user submits by pressing Done [KeyboardActions.onDone] on his keyboard.
 * @param content - List content, that will be rendered under the SearchBar.
 * The suggestions bar will be rendered on top of the [content]
 */
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun SearchBarLayout(
    onSubmit: (String) -> Unit,
    onBack: () -> Unit = {},
    state: SearchBarLayoutState = rememberSearchBarInputState(hint = ""),
    scrollBehavior: TopAppBarScrollBehavior = pinnedScrollBehavior(rememberTopAppBarState()),
    clearContentDescription: String? = null,
    backContentDescription: String? = null,
    content: @Composable () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val textFieldTransition = updateTransition(targetState = state.inputIsVisible, "Animation")
    val focusRequester = remember { FocusRequester() }

    val clearFocus: () -> Unit = remember(keyboardController) {
        {
            keyboardController?.hide()
        }
    }

    val submit: () -> Unit = { onSubmit.invoke(state.input.text.trim()) }

    val shouldRequestFocus = textFieldTransition.targetState && textFieldTransition.currentState

    if (shouldRequestFocus) {
        SideEffect {
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.smallTopAppBarColors(),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = backContentDescription,
                        )
                    }
                },
                title = {
                    if (state.inputIsVisible) {
                        SearchInputLayout(
                            props = SearchInputLayout.Props(
                                query = state.input,
                                hint = state.hint,
                            ),
                            modifier = Modifier.focusRequester(focusRequester),
                            onValueChange = { state.input = it; onSubmit(it.text) },
                            onDone = {
                                clearFocus()
                            },
                            onClear = {
                                state.input = TextFieldValue("")
                                submit()
                            },
                            clearContentDescription = clearContentDescription,
                        )
                    } else {
                        SearchBarTitle(
                            text = state.hint,
                            modifier = Modifier
                                .padding(start = 24.dp)
                                .fillMaxWidth(),
                            onClick = {
                                state.switchVisibility()
                                scrollBehavior.state.heightOffset = 0f
                            },
                        )
                    }
                })
        }, content = { padding ->
            Box(Modifier.padding(padding)) {
                content()
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewSearchBar() {
    SearchBarLayout(
        state = rememberSearchBarInputState(hint = "Hint"),
        onSubmit = {}
    )
}

@Stable
class SearchBarLayoutState(
    private val initialHint: String,
    initialInput: TextFieldValue = TextFieldValue("")
) {
    var input by mutableStateOf(initialInput)

    val hint by derivedStateOf { input.text.ifEmpty { initialHint } }

    var inputIsVisible by mutableStateOf(false)

    fun switchVisibility() {
        inputIsVisible = !inputIsVisible
    }

    companion object {
        val Saver: Saver<SearchBarLayoutState, Any> = listSaver(
            save = {
                listOf(
                    TextFieldValue.Saver.run { save(it.input) },
                    it.initialHint
                )
            },
            restore = {
                val hint = it[1] as String
                val input = TextFieldValue.Saver.restore(it[0]!!)!!
                SearchBarLayoutState(hint, input)
            })
    }
}

@Composable
fun rememberSearchBarInputState(hint: String): SearchBarLayoutState =
    rememberSaveable(hint, saver = SearchBarLayoutState.Saver) {
        SearchBarLayoutState(hint)
    }
