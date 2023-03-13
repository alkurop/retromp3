package com.omar.retromp3recorder.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

/**
 * Invisible composable,that checks for permissions.
 *
 * If [permissions] are not available, launches a Permission Request
 * If permission are available, triggers [onPermissionsGranted]
 *
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionsLayout(
    permissions: List<String>,
    onPermissionsGranted: () -> Unit
) {
    val state = rememberMultiplePermissionsState(
        permissions = permissions
    ) {
        if (it.values.contains(true)) {
            onPermissionsGranted()
        }
    }

    LaunchedEffect(key1 = null) {
        launch {
            state.launchMultiplePermissionRequest()
        }
    }
}
