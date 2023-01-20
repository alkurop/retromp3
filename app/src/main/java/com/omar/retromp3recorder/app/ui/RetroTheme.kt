package com.omar.retromp3recorder.app.ui

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun RetroTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Colors(
            primary = Color.White,
            primaryVariant = Color.White,
            secondaryVariant = Color(0xFFFF6F00),
            secondary = Color(0x9900ff00),
            background = Color(0xff121212),
            surface = Color(0xff141414),
            error = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color.Black,
            onSurface = Color.White,
            onError = Color.White,
            isLight = false
        )
    ) {
        content.invoke()
    }
}