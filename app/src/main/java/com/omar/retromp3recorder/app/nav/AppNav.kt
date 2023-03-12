package com.omar.retromp3recorder.app.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.omar.retromp3recorder.app.ui.menu.popups.crop.CropPopupLayout
import com.omar.retromp3recorder.app.ui.menu.popups.delete.DeletePopupLayout
import com.omar.retromp3recorder.app.ui.menu.popups.rename.RenamePopupLayout

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        startDestination = AppDestination.HomeScreen.route,
        navController = navController,
        modifier = modifier
    ) {
        composable(route = AppDestination.HomeScreen.route) {

        }
        composable(route = AppDestination.SettingScreen.route) {
            TODO()
        }
        composable(route = AppDestination.SearchScreen.route) {
            TODO()
        }
        dialog(AppDestination.CropPopup.route) {
            CropPopupLayout()
        }
        dialog(AppDestination.RenamePopup.route) {
            RenamePopupLayout()
        }
        dialog(AppDestination.DeletePopup.route) {
            DeletePopupLayout()
        }
    }
}

private fun NavHostController.navigate(destination: AppDestination) {
    when (val type = destination.type) {
        DestinationType.Popup -> this.navigatePopup(destination.route)
        DestinationType.Screen -> this.navigateScreen(destination.route)
    }
}

private fun NavHostController.navigatePopup(route: String) {}

private fun NavHostController.navigateScreen(route: String) {}
