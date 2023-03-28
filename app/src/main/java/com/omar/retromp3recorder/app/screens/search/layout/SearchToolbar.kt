package com.omar.retromp3recorder.app.screens.search.layout

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omar.retromp3recorder.app.R


@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SearchToolbar(
    onSearch: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isFocused by remember { mutableStateOf(true) }
    var textFileValue by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val onQueryChange: (TextFieldValue) -> Unit = {
        if (textFileValue.text != it.text) {
            onSearch.invoke(it.text)
            textFileValue = it
        }
    }

    val backCallback = remember {
        {
            if (!isFocused) {
                onBack()
            } else {
                isFocused = false
                focusManager.clearFocus()
            }
            keyboardController?.hide()
        }
    }

    val focusRequester = remember { FocusRequester() }
    AppBar(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        elevation = 4.dp,
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            // Back button
            IconButton(
                modifier = Modifier.padding(start = 2.dp),
                onClick = { backCallback() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }

            // Search box
            Box(Modifier.weight(1f)) {
                BasicTextField(
                    value = textFileValue,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                    ),

                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .onFocusChanged { isFocused = it.isFocused }
                        .focusRequester(focusRequester),
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Uri,
                        autoCorrect = false
                    ),
                )
                // Hint
                if (textFileValue.text.isEmpty()) {
                    Text(text = stringResource(id = R.string.search_hint))
                }
            }
            if (textFileValue.text.isEmpty().not()) {
                // Clear button
                IconButton(
                    modifier = Modifier.padding(start = 2.dp),
                    onClick = {
                        onQueryChange.invoke(TextFieldValue(""))
                    }) { Icon(imageVector = Icons.Default.Close, contentDescription = null) }
            }
        }
    }
}

@Composable
private fun AppBar(
    backgroundColor: Color,
    contentColor: Color,
    elevation: Dp,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = AppBarHorizontalPadding, end = AppBarHorizontalPadding)
                .height(AppBarHeight),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

private val AppBarHeight = 56.dp

private val AppBarHorizontalPadding = 4.dp
