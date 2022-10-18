package com.omar.retromp3recorder.app.ui.files.selector.jpc.layout

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SearchToolbar(
    onsBackPressed: () -> Unit = {},
    onSearch: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val onBackPressed: OnBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    var isFocused by remember { mutableStateOf(true) }
    var textFileValue by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val onQueryChange: (TextFieldValue) -> Unit = {
        textFileValue = it
    }
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!isFocused) {
                    isEnabled = false
                    onBackPressed.onBackPressed()
                } else {
                    isFocused = false
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            }
        }
    }


    DisposableEffect(onBackPressed) { // dispose/relaunch if dispatcher changes
        onBackPressed.addCallback(backCallback)
        onDispose {
            backCallback.remove() // avoid leaks!
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
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onBackPressed
                }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            BasicTextField(

                value = textFileValue,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                ),

                cursorBrush = SolidColor(MaterialTheme.colors.primary),
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused }
                    .focusRequester(focusRequester),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Uri,
                    autoCorrect = false
                ),
            )

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
        elevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
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
}

private val AppBarHeight = 56.dp

private val AppBarHorizontalPadding = 4.dp