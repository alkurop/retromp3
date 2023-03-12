package com.omar.retromp3recorder.app.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.omar.retromp3recorder.app.screens.search.layout.SearchScreenLayout
import com.omar.retromp3recorder.app.screens.main.MainLayout
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop.CropPopupLayout
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete.DeletePopupLayout
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename.RenamePopupLayout
import com.omar.retromp3recorder.app.screens.settings.compose.SettingsLayout

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
            SettingsLayout(onBack = { navController.popBackStack() })
        }
        composable(route = AppDestination.SearchScreen.route) {
            SearchScreenLayout(onBack = { navController.popBackStack() })
        }
        dialog(AppDestination.CropPopup.route) {
            CropPopupLayout(onDismiss = { navController.popBackStack() })
        }
        dialog(AppDestination.RenamePopup.route) {
            RenamePopupLayout(onDismiss = { navController.popBackStack() })
        }
        dialog(AppDestination.DeletePopup.route) {
            DeletePopupLayout(onDismiss = { navController.popBackStack() })
        }
    }
}

fun NavHostController.navigate(destination: AppDestination) {
    when (destination.type) {
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
