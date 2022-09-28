package com.omar.retromp3recorder.app.ui.menu

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.omar.retromp3recorder.dto.AudioEnabler
import com.omar.retromp3recorder.dto.MenuAction
import com.omar.retromp3recorder.dto.MenuExecutable
import com.omar.retromp3recorder.dto.VisibilityEnabler

class MenuPreviewStateProvider : PreviewParameterProvider<MenuView.State> {
    override val values = sequenceOf(
        MenuView.State(
            isVisible = true,
            items = listOf(
                MenuAction.Popup(
                    MenuExecutable.Crop,
                    true
                ),
                MenuAction.Popup(
                    MenuExecutable.Crop,
                    false
                ),
                MenuAction.Enable(
                    VisibilityEnabler.RangeBar,
                    false
                ),
                MenuAction.Enable(
                    AudioEnabler.Loop,
                    true
                ),
                MenuAction.Enable(
                    AudioEnabler.Reverse,
                    false
                ),
                MenuAction.Enable(
                    VisibilityEnabler.PlaybackSpeed,
                    true
                )
            )
        )
    )
}
