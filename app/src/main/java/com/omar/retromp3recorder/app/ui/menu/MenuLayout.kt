package com.omar.retromp3recorder.app.ui.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun MenuLayout(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    MenuPopupNav(navController)

    MenuView(
        modifier = modifier.fillMaxWidth(),
        navController = navController
    )
}
