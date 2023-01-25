package com.omar.retromp3recorder.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val darkColorTheme = darkColorScheme(
    primary = Color.White,
    secondary = Color(0x9900ff00),
    tertiary = Color(0xFFFF6F00),
    background = Color(0xff121212),
    surface = Color(0xff202020),
    error = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White,
)

@Composable
fun RetroTheme(content: @Composable () -> Unit) {
    val colorScheme = darkColorTheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
        }
    }
    MaterialTheme {
        CompositionLocalProvider(
            LocalSpacing provides Spacing(),
            LocalElevation provides Elevation()

        ) {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography,
                content = content
            )
        }
    }
}
