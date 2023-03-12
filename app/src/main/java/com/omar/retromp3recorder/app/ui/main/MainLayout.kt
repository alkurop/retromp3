package com.omar.retromp3recorder.app.ui.main

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.nav.AppDestination
import com.omar.retromp3recorder.app.ui.audio_controls.compose.AudioControlsLayout
import com.omar.retromp3recorder.app.ui.joined_progress.JoinedProgressLayout
import com.omar.retromp3recorder.app.ui.log.compose.LogLayout
import com.omar.retromp3recorder.app.ui.menu.MenuLayout
import com.omar.retromp3recorder.app.ui.rangebar.compose.RangeBarLayout
import com.omar.retromp3recorder.app.ui.settings.SettingsActivity
import com.omar.retromp3recorder.app.ui.theme.LocalSpacing
import com.omar.retromp3recorder.app.ui.visualizer.VisualizerLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    viewModel: MainViewModel = viewModel(),
    onOpenDestination: (AppDestination) -> Unit,
) {
    Column {
        val state by viewModel.state.subscribeAsState(initial = MainViewContract.State())

        val context = LocalContext.current

        TopAppBar(
            title = { Text(text = stringResource(id = R.string.app_name)) },
            actions = {
                IconButton(
                    onClick = {
                        val intent = Intent(context, SettingsActivity::class.java)
                        context.startActivity(intent)
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
                .padding(top = LocalSpacing.current.x_large)
                .height(60.dp)
                .padding(horizontal = LocalSpacing.current.normal)
                .padding(bottom = LocalSpacing.current.normal)
        )
        JoinedProgressLayout(
            modifier = Modifier
                .padding(horizontal = LocalSpacing.current.normal)
                .height(60.dp)
        )
        RangeBarLayout(
            modifier = Modifier
                .padding(horizontal = LocalSpacing.current.normal)
        )
        MenuLayout(onOpenDestination)
        AudioControlsLayout(
            modifier = Modifier
                .padding(
                    horizontal = LocalSpacing.current.medium,
                    vertical = LocalSpacing.current.small
                )
        )
        LogLayout(
            modifier = Modifier
                .alpha(if (state.isLogViewEnabled) 0.6f else 0.0f)
                .height(60.dp)
                .padding(horizontal = LocalSpacing.current.normal)
        )
    }
}
