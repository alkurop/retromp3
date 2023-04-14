package com.omar.retromp3recorder.app

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)

private val RetroShapes = Shapes(
    medium = RoundedCornerShape(4.dp),
)

private val darkColorTheme = darkColorScheme(
    primary = Color.White,
    secondary = Color(0xff00ff00),
    tertiary = Color(0xFFFF6F00),
    background = Color(0xff121212),
    surface = Color(0xff202020),
    surfaceVariant = Color(0xff404040),
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
    if (!view.isInEditMode && view.context is Activity) {
        SideEffect {
            (view.context as? Activity)?.window?.statusBarColor = colorScheme.background.toArgb()
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        shapes = RetroShapes
    )
}

@Composable
fun RetroButtonDark(onClick: () -> Unit, content: @Composable () -> Unit) {
    Button(
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        onClick = onClick,
    ) { content() }
}

@Composable
fun RetroButtonLight(onClick: () -> Unit, content: @Composable () -> Unit) {
    Button(
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        onClick = onClick,
    ) { content() }
}

@Composable
fun RetroProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier, color = MaterialTheme.colorScheme.onPrimary)
}
