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

@Preview
@Composable
fun MenuItemComposable(
    title: String = "menu"
) {
    Column(Modifier.padding(2.dp)) {
        MenuBox(Color.Gray) {
            Column(
                Modifier.padding(bottom = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    title, Modifier.padding(start = 4.dp, end = 4.dp),
                    color = Color.White,
                    fontSize = 12.sp
                )
                Circle(Color.Red)
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