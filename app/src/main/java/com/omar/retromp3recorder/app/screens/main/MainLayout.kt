package com.omar.retromp3recorder.app.screens.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.nav.AppDestination
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.AudioControlsLayout
import com.omar.retromp3recorder.app.screens.main.components.joined_progress.JoinedProgressLayout
import com.omar.retromp3recorder.app.screens.main.components.log.LogLayout
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuView
import com.omar.retromp3recorder.app.screens.main.components.rangebar.RangeBarLayout
import com.omar.retromp3recorder.app.screens.main.components.visualizer.VisualizerLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    viewModel: MainViewModel = hiltViewModel(),
    onOpenDestination: (AppDestination) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current


    val imagePicker = rememberLauncherForActivityResult(
        contract = MediaProjectionActivityResultContract(context),
        onResult = { projection ->
            viewModel.emit(MainViewContract.Input.MediaProjectionUpdated(projection))
        }
    )

    if (state.requestForScreenCapture.ghost != null) {
        SideEffect {
            imagePicker.launch(Unit)
        }
    }

    Column {
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
        Column(
            Modifier
                .weight(1f)
        ) {
            VisualizerLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
            )
            TrackLayout(
                onOpenDestination = onOpenDestination
            )
        }
        Column {
            AudioControlsLayout(
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
                    .height(50.dp)

            )
            LogLayout(
                modifier = Modifier
                    .alpha(if (state.isLogViewEnabled) 0.6f else 0.0f)
                    .height(60.dp)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun TrackLayout(
    onOpenDestination: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        JoinedProgressLayout(
            modifier = Modifier
                .fillMaxWidth()
//                .height(50.dp)
                .height(200.dp)
                .padding(horizontal = 16.dp)

        )
        RangeBarLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        MenuView(onOpenDestination)
    }
}
