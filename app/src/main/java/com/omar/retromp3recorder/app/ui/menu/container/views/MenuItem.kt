package com.omar.retromp3recorder.app.ui.menu.container.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.menu.container.views.layout.MenuItemLayout


enum class MenuItemState {
    EnablerEnabled,
    EnablerDisabled,
    PopupEnabled,
    PopupDisabled
}

@Preview
@Composable
fun MenuItemComposable(
    modifier: Modifier = Modifier,
    title: String = "menu",
    state: MenuItemState = MenuItemState.EnablerDisabled,
) {
    when (state) {
        MenuItemState.EnablerEnabled -> EnablerEnabled(title = title, modifier)
        MenuItemState.EnablerDisabled -> EnablerDisabled(title = title, modifier)
        MenuItemState.PopupEnabled -> PopupEnabled(title = title, modifier)
        MenuItemState.PopupDisabled -> PopupDisabled(title = title, modifier)
    }
}

@Composable
private fun EnablerEnabled(
    title: String,
    modifier: Modifier,
) {
    MenuItemLayout(
        title = title,
        backgroundColor = colorResource(id = R.color.grayish),
        circleColor = Color.Green,
        textColor = Color.Green,
        modifier = modifier
    )
}

@Composable
private fun EnablerDisabled(
    title: String,
    modifier: Modifier,
) {
    MenuItemLayout(
        title = title,
        textColor = Color.Green,
        backgroundColor = colorResource(id = R.color.grayish),
        circleColor = Color.Gray,
        modifier = modifier
    )
}

@Composable
private fun PopupEnabled(
    title: String,
    modifier: Modifier,
) {
    MenuItemLayout(
        title = title,
        backgroundColor = colorResource(id = R.color.grayish),
        circleColor = null,
        textColor = Color.Green,
        modifier = modifier
    )
}

@Composable
private fun PopupDisabled(
    title: String,
    modifier: Modifier,
) {
    MenuItemLayout(
        title = title,
        backgroundColor = colorResource(id = R.color.grayish),
        textColor = colorResource(id = R.color.half_white),
        circleColor = null,
        modifier = modifier
    )
}

