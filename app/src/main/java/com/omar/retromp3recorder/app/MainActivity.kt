package com.omar.retromp3recorder.app

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.omar.retromp3recorder.app.nav.AppNavHost
import com.omar.retromp3recorder.app.screens.main.MainViewContract
import com.omar.retromp3recorder.app.screens.main.MainViewModel
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    @Inject lateinit var toastRepo: ToastRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                this@MainActivity.renderView(state)
            }
        }
        lifecycleScope.launch {
            toastRepo.flow().collect { toast ->
                Toast.makeText(this@MainActivity, toast, Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            RetroTheme {
                val navController = rememberAnimatedNavController()
                AppNavHost(navController = navController)
            }
        }
    }

    private fun renderView(state: MainViewContract.State) {
        state.apply {
            if (shouldKeepScreenOn) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}
