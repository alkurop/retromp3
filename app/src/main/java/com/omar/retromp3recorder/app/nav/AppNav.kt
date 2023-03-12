package com.omar.retromp3recorder.app.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.omar.retromp3recorder.app.ui.files.selector.layout.SearchScreenLayout
import com.omar.retromp3recorder.app.ui.main.MainLayout
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
            MainLayout(onOpenDestination = { navController.navigate(it) })
        }
        composable(route = AppDestination.SettingScreen.route) {
            SearchScreenLayout()
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

fun NavHostController.navigate(destination: AppDestination) {
    when (val type = destination.type) {
        DestinationType.Popup -> this.navigatePopup(destination.route)
        DestinationType.Screen -> this.navigateScreen(destination.route)
    }
}

fun NavHostController.navigatePopup(route: String) {
    this.navigate(route)
}

fun NavHostController.navigateScreen(route: String) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}
