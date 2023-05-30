package com.omar.retromp3recorder.app.screens.home.components.menu.views.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun MenuItemLayout(
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
                title.lowercase(Locale.ROOT),
                Modifier.padding(start = 4.dp, bottom = 2.dp, end = 4.dp),
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
    Box(modifier.height(40.dp)) {
        Box(
            Modifier
                .background(
                    bgColor,
                    shape = RoundedCornerShape(0.dp, 0.dp, corners, corners)
                )

        ) { content() }
    }
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
