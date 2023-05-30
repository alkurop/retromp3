package com.omar.retromp3recorder.app.main

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.omar.retromp3recorder.app.RetroTheme
import com.omar.retromp3recorder.app.billing.BillingInteractor
import com.omar.retromp3recorder.app.nav.AppNavHost
import com.omar.retromp3recorder.app.screens.landing.LandingScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    // Billing goes outside of the ViewModel, because it uses Activity component
    @Inject
    lateinit var billing: BillingInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        lifecycleScope.launch { billing.setup() }
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                this@MainActivity.renderView(state)
            }
        }

        requestPermissions(arrayOf("android.permission.POST_NOTIFICATIONS"), 7)
        setContent {
            RetroTheme {
                var showLandingScreen by remember { mutableStateOf(true) }
                val navController = rememberAnimatedNavController()

                if (showLandingScreen) {
                    LandingScreen(onLoaded = { showLandingScreen = false })
                } else {
                    AppNavHost(navController = navController)
                }
            }
        }
    }

    private fun renderView(state: MainActivityContract.State) {
        state.apply {
            if (shouldKeepScreenOn) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            toast.ghost?.let {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
