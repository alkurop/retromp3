package com.omar.retromp3recorder.app.screens.home.components.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.nav.AppDestination
import com.omar.retromp3recorder.app.screens.home.components.menu.views.layout.MenuLayout
import com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic.MenuViewModel
import com.omar.retromp3recorder.domain.MenuPopup


@Composable
fun MenuLayout(
    onOpenDestination: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuViewModel = hiltViewModel(),
) {
    val state: MenuContract.State by viewModel.state.collectAsState()
    MenuLayout(
        modifier = modifier,
        state = state,
        onAction = {
            viewModel.onEvent(it)
        },
        onNavigation = { onOpenDestination(it.toAppDestination()) }
    )
}

private fun MenuPopup?.toAppDestination(): AppDestination =
    when (this) {
        null -> AppDestination.HomeScreen
        MenuPopup.Crop -> AppDestination.CropPopup
        MenuPopup.Delete -> AppDestination.DeletePopup
        MenuPopup.Search -> AppDestination.SearchScreen
        MenuPopup.Rename -> AppDestination.RenamePopup
    }
