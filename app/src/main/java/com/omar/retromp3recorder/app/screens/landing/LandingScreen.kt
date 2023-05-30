package com.omar.retromp3recorder.app.screens.landing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.RetroProgressIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LandingScreen(
    onLoaded: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LandingViewModel = hiltViewModel()

) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        RetroProgressIndicator(
            Modifier.size(50.dp)
        )
    }

    LaunchedEffect("") {
            viewModel.load()
        onLoaded()
    }
}

