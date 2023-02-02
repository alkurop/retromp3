package com.omar.retromp3recorder.app.ui.menu

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
import com.omar.retromp3recorder.app.ui.files.selector.SelectorActivityJPC
import com.omar.retromp3recorder.app.ui.menu.popups.crop.CropPopupLayout
import com.omar.retromp3recorder.app.ui.menu.popups.delete.DeletePopupLayout
import com.omar.retromp3recorder.app.ui.menu.popups.rename.RenamePopupLayout
import com.omar.retromp3recorder.app.ui.menu.views.MenuViewModel
import com.omar.retromp3recorder.app.ui.menu.views.layout.MenuLayout
import com.omar.retromp3recorder.dto.MenuPopup


@Composable
fun MenuView(
    modifier: Modifier,
    viewModel: MenuViewModel = viewModel(),
    navController: NavHostController,
) {
    val state: MenuContract.State by viewModel.state.subscribeAsState(initial = MenuContract.State())
    MenuLayout(
        modifier,
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
        if (popup == MenuPopup.Search) {
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
    NavHost(navController = navController, startDestination = START_WITH_BLANK) {
        composable(START_WITH_BLANK) {
            Spacer(modifier = Modifier)
        }
        dialog(MenuPopup.Crop.name) {
            CropPopupLayout()
        }
        dialog(MenuPopup.Rename.name) {
            RenamePopupLayout()
        }
        dialog(MenuPopup.Delete.name) {
            DeletePopupLayout()
        }
    }
}

private const val START_WITH_BLANK = "BLANK"
