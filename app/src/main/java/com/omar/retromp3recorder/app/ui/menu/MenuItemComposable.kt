package com.omar.retromp3recorder.app.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
        backgroundColor = Color.Black,
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
        backgroundColor = Color.Black,
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
    Column(modifier.padding(2.dp)) {
        MenuBox(backgroundColor) {
            Column(
                Modifier.padding(bottom = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    title, Modifier.padding(start = 4.dp, end = 4.dp),
                    color = Color.White,
                    fontSize = 12.sp
                )
                if (circleColor != null) Circle(circleColor) else Spacer(modifier = Modifier)
            }
        }
    }
}

@Composable
private fun MenuBox(
    color: Color,
    content: @Composable () -> Unit
) {
    Box(
        Modifier
            .offset(y = (-1).dp)

            .background(
                color,
                shape = RoundedCornerShape(0.dp, 0.dp, corners, corners)
            )
            .border(
                width = 1.dp,
                color = Color.Green,
                shape = RoundedCornerShape(0.dp, 0.dp, corners, corners)
            )

    ) { content() }
}


@Composable
private fun Circle(color: Color) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(top = 1.dp, bottom = 1.dp)
            .size(5.dp)
            .clip(CircleShape)
            .background(color)
    )
}


private val corners = 8.dp

