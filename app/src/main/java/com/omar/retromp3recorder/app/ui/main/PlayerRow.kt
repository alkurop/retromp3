package com.omar.retromp3recorder.app.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.omar.retromp3recorder.app.ui.audio_controls.compose.AudioControlsLayout
import com.omar.retromp3recorder.app.ui.joined_progress.JoinedProgressLayout
import com.omar.retromp3recorder.app.ui.menu.MenuLayout
import com.omar.retromp3recorder.app.ui.rangebar.compose.RangeBarLayout
import com.omar.retromp3recorder.app.ui.theme.LocalSpacing


@Composable
fun PlayerRow() {
    Column {
        JoinedProgressLayout(
            modifier = Modifier
                .padding(
                    horizontal = LocalSpacing.current.normal
                )
                .height(60.dp)
        )
        RangeBarLayout(
            modifier = Modifier
                .padding(
                    horizontal = LocalSpacing.current.normal,
                )
        )

        MenuLayout()

        AudioControlsLayout(
            modifier = Modifier
                .padding(
                    horizontal = LocalSpacing.current.medium,
                    vertical = LocalSpacing.current.small
                )
        )
    }
}
