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


enum class MenuItemState {
    Enabled,
    Disabled,
    OneTime
}

@Preview
@Composable
fun MenuItemComposable(
    modifier: Modifier = Modifier,
    title: String = "menu",
    state: MenuItemState = MenuItemState.Disabled,
) {
    when (state) {
        MenuItemState.Enabled -> MenuItemEnabled(title = title, modifier)
        MenuItemState.OneTime -> MenuItemOneTime(title = title, modifier)
        MenuItemState.Disabled -> MenuItemDisabled(title = title, modifier)
    }
}

@Composable
private fun MenuItemEnabled(
    title: String,
    modifier: Modifier,
) {
    MenuItemConstructor(
        title = title,
        backgroundColor = Color.Gray,
        circleColor = Color.Green,
        modifier = modifier
    )
}

@Composable
private fun MenuItemDisabled(
    title: String,
    modifier: Modifier,
) {
    MenuItemConstructor(
        title = title,
        backgroundColor = colorResource(id = R.color.grayish),
        circleColor = Color.Gray,
        modifier = modifier
    )
}

@Composable
private fun MenuItemOneTime(
    title: String,
    modifier: Modifier,
) {
    MenuItemConstructor(
        title = title,
        backgroundColor = colorResource(id = R.color.grayish),
        circleColor = null,
        modifier = modifier
    )
}

@Composable
private fun MenuItemConstructor(
    title: String,
    backgroundColor: Color,
    circleColor: Color?,
    modifier: Modifier,
) {
    MenuBox(modifier, backgroundColor) {
        Column(
            Modifier.padding(bottom = 2.dp).width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title.toLowerCase(), Modifier.padding(start = 4.dp, end = 4.dp),
                color = Color.White,
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
            .padding(top = 3.dp, bottom = 2.dp)
            .size(5.dp)
            .clip(CircleShape)
            .background(color)
    )
}


private val corners = 8.dp
