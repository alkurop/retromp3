package com.omar.retromp3recorder.app.screens.main.components.menu.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.main.components.menu.views.layout.MenuItemLayout

@Composable
fun EnablerMenuItem(
    isOpen: Boolean,
    isEnabled: Boolean,
    title: String,
    modifier: Modifier = Modifier,
) {
    MenuItemLayout(
        title = title,
        backgroundColor = colorResource(id = R.color.grayish),
        circleColor = if (isEnabled) Color.Green else Color.Gray,
        textColor = if (isOpen) Color.White else Color.Green,
        modifier = modifier
    )
}


@Composable
fun PopupMenuItem(
    isEnabled: Boolean,
    title: String,
    modifier: Modifier = Modifier,
) {
    MenuItemLayout(
        title = title,
        backgroundColor = colorResource(id = R.color.grayish),
        circleColor = null,
        textColor = if (isEnabled) Color.Green else colorResource(id = R.color.half_white),
        modifier = modifier
    )
}
