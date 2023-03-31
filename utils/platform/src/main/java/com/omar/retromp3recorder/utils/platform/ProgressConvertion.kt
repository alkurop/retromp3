package com.omar.retromp3recorder.utils.platform

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.Duration

typealias PlayerTime = Long

data class TimeDisplay(
    val time: String,
    val millis: String
)

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

private const val DATE_FORMAT_PATTERN = ("dd-MMM-yyyy HH:mm")

@SuppressLint("SimpleDateFormat")
private val formatter = SimpleDateFormat(DATE_FORMAT_PATTERN)
fun Long.toCreationDate(): String {
    return formatter.format(this)
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
