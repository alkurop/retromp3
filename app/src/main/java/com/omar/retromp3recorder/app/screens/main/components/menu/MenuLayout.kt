package com.omar.retromp3recorder.app.screens.main.components.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.nav.AppDestination
import com.omar.retromp3recorder.app.screens.main.components.menu.views.MenuViewModel
import com.omar.retromp3recorder.app.screens.main.components.menu.views.layout.MenuLayout
import com.omar.retromp3recorder.domain.MenuPopup


@Composable
fun MenuView(
    onOpenDestination: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuViewModel = viewModel(),
) {
    val state: MenuContract.State by viewModel.state.subscribeAsState(initial = MenuContract.State())
    MenuLayout(
        modifier = modifier,
        state = state,
        onAction = {
            viewModel.input.onNext(it)
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
