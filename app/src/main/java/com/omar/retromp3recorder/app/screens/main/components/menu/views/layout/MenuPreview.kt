package com.omar.retromp3recorder.app.screens.main.components.menu.views.layout

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.domain.AudioEnabler
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.domain.VisibilityEnabler

class MenuPreviewStateProvider : PreviewParameterProvider<MenuContract.State> {
    override val values = sequenceOf(
        MenuContract.State(
            isVisible = true,
            items = listOf(
                MenuContract.Item.Popup(
                    MenuPopup.Crop,
                    true
                ),
                MenuContract.Item.Popup(
                    MenuPopup.Search,
                    false
                ),
                MenuContract.Item.Enable(
                    VisibilityEnabler.RangeBar,
                    false,
                    false
                ),
                MenuContract.Item.Enable(
                    AudioEnabler.Loop,
                    true,
                    true
                ),
                MenuContract.Item.Enable(
                    AudioEnabler.Reverse,
                    false,
                    true
                ),
                MenuContract.Item.Enable(
                    VisibilityEnabler.PlaybackSpeed,
                    true,
                    true
                )
            )
        )
    )
}
