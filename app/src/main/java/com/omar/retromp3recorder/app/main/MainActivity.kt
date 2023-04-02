package com.omar.retromp3recorder.app.main

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.omar.retromp3recorder.app.RetroTheme
import com.omar.retromp3recorder.app.nav.AppNavHost
import com.omar.retromp3recorder.io.billing.Billing
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var billing: Billing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                this@MainActivity.renderView(state)
            }
        }

        setContent {
            RetroTheme {
                val navController = rememberAnimatedNavController()
                AppNavHost(navController = navController)
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
