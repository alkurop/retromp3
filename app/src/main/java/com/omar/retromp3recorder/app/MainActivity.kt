package com.omar.retromp3recorder.app

import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.omar.retromp3recorder.app.nav.AppNavHost
import com.omar.retromp3recorder.app.screens.main.MainViewContract
import com.omar.retromp3recorder.app.screens.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private val mediaProjectionManager by lazy { getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                this@MainActivity.renderView(state)
            }
        }
        lifecycleScope.launch {
            viewModel.toastRepo.flow().collect { toast ->
                Toast.makeText(this@MainActivity, toast, Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            RetroTheme {
                val navController = rememberNavController()
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
            requestForScreenCapture.ghost?.let { makeScreenCaptureRequest() }
        }
    }

    private fun makeScreenCaptureRequest() {
        @Suppress("DEPRECATED_METHOD")
        startActivityForResult(
            mediaProjectionManager.createScreenCaptureIntent(),
            MEDIA_PROJECTION_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MEDIA_PROJECTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                val projection = mediaProjectionManager.getMediaProjection(resultCode, data)
                viewModel.emit(MainViewContract.Input.MediaProjectionUpdated(projection))
                projection.registerCallback(object : MediaProjection.Callback() {
                    override fun onStop() {
                        viewModel.emit(MainViewContract.Input.MediaProjectionUpdated(null))
                    }
                }, Handler(Looper.myLooper()!!))
            } else {
                viewModel.emit(MainViewContract.Input.MediaProjectionUpdated(null))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private companion object {
        const val MEDIA_PROJECTION_REQUEST_CODE = 22
    }
}
