package com.omar.retromp3recorder.app.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.nav.AppDestination
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.AudioControlsLayout
import com.omar.retromp3recorder.app.screens.main.components.joined_progress.JoinedProgressLayout
import com.omar.retromp3recorder.app.screens.main.components.log.LogLayout
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuView
import com.omar.retromp3recorder.app.screens.main.components.rangebar.RangeBarLayout
import com.omar.retromp3recorder.app.screens.main.components.visualizer.VisualizerLayout
import com.omar.retromp3recorder.app.utils.RequestPermissionsLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    viewModel: MainViewModel = viewModel(),
    onOpenDestination: (AppDestination) -> Unit,
) {
    Column {
        val state by viewModel.state.subscribeAsState(initial = MainViewContract.State())

        TopAppBar(
            title = { Text(text = stringResource(id = R.string.app_name)) },
            actions = {
                IconButton(
                    onClick = {
                        onOpenDestination(AppDestination.SettingScreen)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(id = R.string.settings)
                    )
                }
            }
        )
        VisualizerLayout(
            modifier = Modifier
                .padding(top = 32.dp)
                .height(60.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        )
        JoinedProgressLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(60.dp)
        )
        RangeBarLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        MenuView(onOpenDestination)
        AudioControlsLayout(
            modifier = Modifier
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
        )
        LogLayout(
            modifier = Modifier
                .alpha(if (state.isLogViewEnabled) 0.6f else 0.0f)
                .height(60.dp)
                .padding(horizontal = 8.dp)
        )
        state.requestForPermissions.ghost?.let {
            RequestPermissionsLayout(permissions = it.toList()) {
                // noop here. Just wait until user does something
            }
        }
    }
}
