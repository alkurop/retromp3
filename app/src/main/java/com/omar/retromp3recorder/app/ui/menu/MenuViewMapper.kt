package com.omar.retromp3recorder.app.ui.menu

import androidx.annotation.StringRes
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.menu.views.MenuItemState
import com.omar.retromp3recorder.domain.AudioEnabler
import com.omar.retromp3recorder.domain.MenuEnabler
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.domain.VisibilityEnabler

@StringRes
fun MenuPopup.getTitleRes(): Int = when (this) {
    MenuPopup.Crop -> R.string.menu_crop
    MenuPopup.Search -> R.string.menu_search
    MenuPopup.Delete -> R.string.menu_delete
    MenuPopup.Rename -> R.string.menu_rename
}

@StringRes
fun MenuEnabler.getTitleRes(): Int = when (this) {
    is AudioEnabler -> this.getTitleRes()
    is VisibilityEnabler -> this.getTitleRes()
}

@StringRes
fun AudioEnabler.getTitleRes(): Int = when (this) {
    AudioEnabler.Reverse -> R.string.menu_reverse
    AudioEnabler.Loop -> R.string.menu_loop
}

@StringRes
fun VisibilityEnabler.getTitleRes(): Int = when (this) {
    VisibilityEnabler.RangeBar -> R.string.menu_range
    VisibilityEnabler.PlaybackSpeed -> R.string.menu_bark_speed
}

fun Boolean.mapToStateEnabler(): MenuItemState {
    return if (this) MenuItemState.EnablerEnabled else MenuItemState.EnablerDisabled
}

fun Boolean.mapToStatePopup(): MenuItemState {
    return if (this) MenuItemState.PopupEnabled else MenuItemState.PopupDisabled
}

