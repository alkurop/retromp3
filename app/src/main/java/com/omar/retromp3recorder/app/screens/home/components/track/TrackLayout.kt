package com.omar.retromp3recorder.app.screens.home.components.track

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.nav.AppDestination
import com.omar.retromp3recorder.app.screens.home.components.joined_progress.JoinedProgressLayout
import com.omar.retromp3recorder.app.screens.home.components.menu.MenuLayout
import com.omar.retromp3recorder.app.screens.home.components.rangebar.RangeBarLayout
import com.omar.retromp3recorder.app.screens.home.components.speedbar.SpeedBarLayout

@Composable
fun TrackLayout(
    onOpenDestination: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TrackViewModel = hiltViewModel()
) {
    viewModel.bind()
    Column(modifier.fillMaxWidth()) {
        JoinedProgressLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 16.dp)

        )
        RangeBarLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        SpeedBarLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        MenuLayout(onOpenDestination)
    }
}
