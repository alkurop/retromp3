package com.omar.retromp3recorder.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacing(
    val xx_small: Dp = 2.dp,
    val x_small: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val normal: Dp = 16.dp,
    val large: Dp = 24.dp,
    val x_large: Dp = 32.dp,
)

val LocalSpacing = compositionLocalOf { Spacing() }
