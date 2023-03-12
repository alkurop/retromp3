package com.omar.retromp3recorder.app.screens.settings.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.settings.compose.audio_source.AudioSourceLayout
import com.omar.retromp3recorder.app.screens.settings.compose.bit_rate.BitRateLayout
import com.omar.retromp3recorder.app.screens.settings.compose.sample_rate.SampleRateLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsLayout(onBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(id = R.string.settings))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                }
            )
        }, content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
            ) {
                item { AudioSourceLayout(Modifier.padding(ITEM_PADDING)) }
                item { BitRateLayout(Modifier.padding(ITEM_PADDING)) }
                item { SampleRateLayout(Modifier.padding(ITEM_PADDING)) }
            }
        }
    )
}
private val ITEM_PADDING = 16.dp
