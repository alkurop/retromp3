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
            secondary = Color.White,
            secondaryVariant = Color.White,
            background = Color.White,
            surface = Color.White,
            error = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color.White,
            onSurface = Color.White,
            onError = Color.White,
            isLight = false
        )
    ) {
        content.invoke()
    }
}