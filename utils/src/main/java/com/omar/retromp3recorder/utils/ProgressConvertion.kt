package com.omar.retromp3recorder.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import androidx.annotation.StyleRes
import com.omar.retromp3recorder.utils.Constants.PRECISION_PLAYER_TO_RECORDER_CONVERSION_MILLIS
import java.time.Duration

typealias SeekbarTime = Int
typealias PlayerTime = Long

data class TimeDisplay(
    val time: String,
    val millis: String
)

fun SeekbarTime.toPlayerTime(): PlayerTime = this.toLong() * PRECISION_PLAYER_TO_RECORDER_CONVERSION_MILLIS
fun PlayerTime.toSeekbarTime(): SeekbarTime = (this / PRECISION_PLAYER_TO_RECORDER_CONVERSION_MILLIS).toInt()


@SuppressLint("NewApi")
fun PlayerTime.toTimeDisplay(): TimeDisplay {
    var duration = Duration.ofMillis(this)
    val hours = duration.toHours()
    duration = duration.minusHours(hours)
    val minutes = duration.toMinutes()
    duration = duration.minusMinutes(minutes)
    val seconds = duration.seconds
    duration = duration.minusSeconds(seconds)

    val millis = duration.toMillis() / 100
    val hasHours = hours > 0
    val hasMinutes = hasHours || minutes > 0
    val hoursString = hours.toFormat(false)
    val hoursSeparator = if (hasHours) ":" else ""
    val minutesString = minutes.toFormat(hasHours)
    val minutesSeparator = if (hasMinutes) ":" else ""
    val secondsString = "${seconds.toFormat(true)}."
    val millisString = "$millis"

    return TimeDisplay(
        hoursString + hoursSeparator + minutesString + minutesSeparator + secondsString,
        millisString
    )
}

private fun Long.toFormat(showFullTime: Boolean): String =
    if (showFullTime && this >= 10) {
        String.format(TIME_FORMAT, this)
    } else if (showFullTime || this > 0) {
        "$this"
    } else {
        ""
    }

private const val TIME_FORMAT = "%02d"

fun TimeDisplay.toSpannableStringWithSmallMillis(
    context: Context,
    @StyleRes textAppearance: Int
): SpannableString {
    val endString = this.time + this.millis
    val start = this.time.length
    val end = endString.length
    val spannable = SpannableString(endString)
    spannable.setSpan(
        TextAppearanceSpan(context, textAppearance),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannable
}
