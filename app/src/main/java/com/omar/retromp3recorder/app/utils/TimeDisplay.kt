package com.omar.retromp3recorder.app.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.omar.retromp3recorder.utils.domain.toTimeDisplay

object TimeDisplay {

    @Composable
    fun Long?.toDisplayCompose(): AnnotatedString {
        return if (this == null) buildAnnotatedString { }
        else buildAnnotatedString {
            val timeDisplay = toTimeDisplay()
            append(timeDisplay.time)
            withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.labelSmall.fontSize)) {
                append(timeDisplay.millis)
            }
        }
    }
}
