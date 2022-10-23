package com.omar.retromp3recorder.app.ui.menu.container.views

import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.omar.retromp3recorder.app.ui.files.selector.jpc.SelectorActivityJPC
import com.omar.retromp3recorder.app.ui.files.selector.jpc.layout.SearchLayout
import com.omar.retromp3recorder.app.ui.menu.container.logic.MenuContract
import com.omar.retromp3recorder.app.ui.menu.container.views.layout.MenuLayout
import com.omar.retromp3recorder.app.ui.menu.popups.crop.CropPopup
import com.omar.retromp3recorder.dto.MenuExecutable


@Composable
fun MenuView(viewModel: MenuViewModel = viewModel(), navController: NavHostController) {
    val state: MenuContract.State by viewModel.state.subscribeAsState(initial = MenuContract.State())
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
        if (popup == MenuExecutable.Open) {
            viewModel.input.onNext(MenuContract.Input.Clear)
            val context = LocalContext.current
            context.startActivity(Intent(context, SelectorActivityJPC::class.java))
        } else {
            navController.navigate(popup.name)
        }
    }
}

@Composable
fun MenuPopupNav(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = BLANK) {
        dialog(MenuExecutable.Crop.name) {
            CropPopup()
        }
        composable(MenuExecutable.Open.name) {
            SearchLayout()
        }
        composable(BLANK) { Spacer(modifier = Modifier) }
    }
}

private const val BLANK = "BLANK"
