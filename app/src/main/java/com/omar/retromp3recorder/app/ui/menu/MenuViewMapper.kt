package com.omar.retromp3recorder.app.ui.menu

import androidx.annotation.StringRes
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.dto.*

@StringRes
fun MenuExecutable.getTitleRes(): Int = when (this) {
    MenuExecutable.Crop -> R.string.menu_bark_crop
}


@StringRes
fun MenuEnabler.getTitleRes(): Int = when (this) {
    is AudioEnabler -> this.getTitleRes()
    is VisibilityEnabler -> this.getTitleRes()
}


@StringRes
fun AudioEnabler.getTitleRes(): Int = when (this) {
    AudioEnabler.Reverse -> R.string.menu_bark_reverse
    AudioEnabler.Loop -> R.string.menu_bark_loop
}

@StringRes
fun VisibilityEnabler.getTitleRes(): Int = when (this) {
    VisibilityEnabler.RangeBar -> R.string.menu_bark_range
    VisibilityEnabler.PlaybackSpeed -> R.string.menu_bark_speed
}

fun Boolean.mapToStateEnabler(): MenuItemState {
    return if (this) MenuItemState.EnablerEnabled else MenuItemState.EnablerDisabled
}

fun Boolean.mapToStatePopup(): MenuItemState {
    return if (this) MenuItemState.PopupEnabled else MenuItemState.PopupDisabled
}

