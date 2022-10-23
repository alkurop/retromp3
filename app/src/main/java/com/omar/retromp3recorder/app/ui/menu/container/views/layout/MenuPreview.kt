package com.omar.retromp3recorder.app.ui.menu.container.views.layout

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.omar.retromp3recorder.app.ui.menu.container.logic.MenuContract
import com.omar.retromp3recorder.dto.AudioEnabler
import com.omar.retromp3recorder.dto.MenuExecutable
import com.omar.retromp3recorder.dto.VisibilityEnabler

class MenuPreviewStateProvider : PreviewParameterProvider<MenuContract.State> {
    override val values = sequenceOf(
        MenuContract.State(
            isVisible = true,
            items = listOf(
                MenuContract.Item.Popup(
                    MenuExecutable.Crop,
                    true
                ),
                MenuContract.Item.Popup(
                    MenuExecutable.Search,
                    false
                ),
                MenuContract.Item.Enable(
                    VisibilityEnabler.RangeBar,
                    false
                ),
                MenuContract.Item.Enable(
                    AudioEnabler.Loop,
                    true
                ),
                MenuContract.Item.Enable(
                    AudioEnabler.Reverse,
                    false
                ),
                MenuContract.Item.Enable(
                    VisibilityEnabler.PlaybackSpeed,
                    true
                )
            )
        )
    )
}
