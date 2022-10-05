package com.omar.retromp3recorder.app.ui.menu.container.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.omar.retromp3recorder.app.ui.menu.container.logic.MenuView
import com.omar.retromp3recorder.app.ui.menu.container.views.layout.MenuLayout
import com.omar.retromp3recorder.app.ui.menu.popups.crop.CropPopup
import com.omar.retromp3recorder.dto.MenuExecutable


@Composable
fun MenuView(viewModel: MenuViewModel = viewModel(), navController: NavHostController) {
    val state: MenuView.State by viewModel.state.subscribeAsState(initial = MenuView.State())
    MenuLayout(
        state = state,
        onAction = {
            viewModel.input.onNext(it)
        })

    val popup = state.popup.value
    if (popup == null) {
        do {
            val pop = navController.popBackStack()
        } while (pop)
    } else {
        navController.navigate(popup.name)
    }
}

@Composable
fun MenuNav(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = BLANK) {
        dialog(MenuExecutable.Crop.name) {
            CropPopup()
        }
        composable(BLANK) { Spacer(modifier = Modifier) }
    }
}

private const val BLANK = "BLANK"
