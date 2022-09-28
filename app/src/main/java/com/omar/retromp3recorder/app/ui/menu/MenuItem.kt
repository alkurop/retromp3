package com.omar.retromp3recorder.app.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omar.retromp3recorder.app.R
import java.util.*


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
        MenuItemState.EnablerEnabled -> MenuItemEnablerEnabled(title = title, modifier)
        MenuItemState.EnablerDisabled -> MenuItemEnablerDisabled(title = title, modifier)
        MenuItemState.PopupEnabled -> MenuItemExecutorEnabled(title = title, modifier)
        MenuItemState.PopupDisabled -> MenuItemExecutorDisabled(title = title, modifier)
    }
}

@Composable
private fun MenuItemEnablerEnabled(
    title: String,
    modifier: Modifier,
) {
    MenuItemConstructor(
        title = title,
        backgroundColor = Color.Gray,
        circleColor = Color.Green,
        textColor = Color.Green,
        modifier = modifier
    )
}

@Composable
private fun MenuItemEnablerDisabled(
    title: String,
    modifier: Modifier,
) {
    MenuItemConstructor(
        title = title,
        textColor = colorResource(id = R.color.half_white),
        backgroundColor = colorResource(id = R.color.grayish),
        circleColor = Color.Gray,
        modifier = modifier
    )
}

@Composable
private fun MenuItemExecutorEnabled(
    title: String,
    modifier: Modifier,
) {
    MenuItemConstructor(
        title = title,
        backgroundColor = colorResource(id = R.color.grayish),
        circleColor = null,
        textColor = Color.Green,
        modifier = modifier
    )
}

@Composable
private fun MenuItemExecutorDisabled(
    title: String,
    modifier: Modifier,
) {
    MenuItemConstructor(
        title = title,
        backgroundColor = colorResource(id = R.color.grayish),
        textColor = colorResource(id = R.color.half_white),
        circleColor = null,
        modifier = modifier
    )
}

@Composable
private fun MenuItemConstructor(
    title: String,
    backgroundColor: Color,
    circleColor: Color?,
    textColor: Color,
    modifier: Modifier,
) {
    MenuBox(modifier, backgroundColor) {
        Column(
            Modifier
                .padding(bottom = 2.dp)
                .width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title.lowercase(Locale.ROOT), Modifier.padding(start = 4.dp, bottom = 2.dp, end = 4.dp),
                color = textColor,
                fontSize = 12.sp
            )
            if (circleColor != null) Circle(circleColor) else Empty()
        }
    }
}

@Composable
private fun Empty() {
    Spacer(
        modifier = Modifier
    )
}

@Composable
private fun MenuBox(
    modifier: Modifier,
    bgColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier
            .background(
                bgColor,
                shape = RoundedCornerShape(0.dp, 0.dp, corners, corners)
            )

    ) { content() }
}


@Composable
private fun Circle(color: Color) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(top = 1.dp, bottom = 2.dp)
            .size(5.dp)
            .clip(CircleShape)
            .background(color)
    )
}


private val corners = 8.dp
