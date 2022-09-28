package com.omar.retromp3recorder.app.ui.menu

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.omar.retromp3recorder.app.ui.menu.logic.MenuView
import com.omar.retromp3recorder.dto.AudioEnabler
import com.omar.retromp3recorder.dto.MenuExecutable
import com.omar.retromp3recorder.dto.VisibilityEnabler

class MenuPreviewStateProvider : PreviewParameterProvider<MenuView.State> {
    override val values = sequenceOf(
        MenuView.State(
            isVisible = true,
            items = listOf(
                MenuView.Item.Popup(
                    MenuExecutable.Crop,
                    true
                ),
                MenuView.Item.Popup(
                    MenuExecutable.Crop,
                    false
                ),
                MenuView.Item.Enable(
                    VisibilityEnabler.RangeBar,
                    false
                ),
                MenuView.Item.Enable(
                    AudioEnabler.Loop,
                    true
                ),
                MenuView.Item.Enable(
                    AudioEnabler.Reverse,
                    false
                ),
                MenuView.Item.Enable(
                    VisibilityEnabler.PlaybackSpeed,
                    true
                )
            )
        )
    )
}
