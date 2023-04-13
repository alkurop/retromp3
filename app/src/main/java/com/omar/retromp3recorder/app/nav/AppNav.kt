package com.omar.retromp3recorder.app.nav

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.dialog
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.omar.retromp3recorder.app.screens.home.HomeLayout
import com.omar.retromp3recorder.app.screens.home.components.menu.popups.crop.CropPopupLayout
import com.omar.retromp3recorder.app.screens.home.components.menu.popups.delete.DeletePopupLayout
import com.omar.retromp3recorder.app.screens.home.components.menu.popups.rename.RenamePopupLayout
import com.omar.retromp3recorder.app.screens.home.components.menu.popups.speech.SpeechPopupLayout
import com.omar.retromp3recorder.app.screens.search.layout.SearchScreenLayout
import com.omar.retromp3recorder.app.screens.settings.SettingsLayout

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        startDestination = AppDestination.HomeScreen.route,
        navController = navController,
        modifier = modifier
    ) {
        composable(
            AppDestination.HomeScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    AppDestination.SearchScreen.route ->
                        slideInHorizontally { -it }
                    else -> fadeIn()
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    AppDestination.SearchScreen.route ->
                        slideOutHorizontally { -it }
                    else -> fadeOut()
                }
            },
        ) {
            HomeLayout(onOpenDestination = { navController.navigate(it) })
        }
        composable(
            route = AppDestination.SettingScreen.route,
            enterTransition = {
                slideInVertically { it }
            },
            exitTransition = {
                slideOutVertically { it }
            },
        ) {
            SettingsLayout(onBack = {
                navController.popBackStack()
            })
        }
        composable(
            route = AppDestination.SearchScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    AppDestination.HomeScreen.route ->
                        slideInHorizontally { it }
                    else -> fadeIn()
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    AppDestination.HomeScreen.route ->
                        slideOutHorizontally { it }
                    else -> fadeOut()
                }
            },
        ) {
            SearchScreenLayout(onBack = {
                navController.popBackStack()
            })
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
        dialog(AppDestination.SpeechRecognitionPopup.route) {
            SpeechPopupLayout {
                navController.popBackStack()
            }
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
