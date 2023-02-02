package com.omar.retromp3recorder.app.ui.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.isInvisible
import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails
import com.github.alkurop.jpermissionmanager.PermissionRequiredDetails
import com.github.alkurop.jpermissionmanager.PermissionsManager
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.settings.SettingsActivity
import com.omar.retromp3recorder.app.ui.theme.LocalSpacing
import com.omar.retromp3recorder.app.ui.theme.RetroTheme
import com.omar.retromp3recorder.app.ui.visualizer.VisualizerLayout
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.app.ui.log.compose.LogLayout

class MainActivity : AppCompatActivity() {
    private val permissionsManager: PermissionsManager by lazy { PermissionsManager(this) }
    private val permissionsMap = createPermissionsMap()
    private val viewModel by viewModels<MainViewModel>()
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    private val composeRowContainer by lazy { findViewById<ComposeView>(R.id.compose_row_container) }
    private val composeLog by lazy { findViewById<ComposeView>(R.id.audio_log_compose) }
    private val visualiser by lazy { findViewById<ComposeView>(R.id.compose_visualizer) }
    private val mediaProjectionManager by lazy { getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        viewModel.state.observe(this, ::renderView)
        viewModel.input.onNext(MainView.Input.CheckAllPermisionsOnStartup)
        viewModel.toastRepo.observe().observe(this) { toast ->
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
        }

        visualiser.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                RetroTheme {
                    VisualizerLayout(
                        modifier = Modifier
                            .padding(
                                horizontal = LocalSpacing.current.normal,
                            )
                            .padding(
                                bottom = LocalSpacing.current.normal,
                            )
                    )
                }
            }
        }
        composeRowContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                RetroTheme {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        PlayerRow()
                    }

                }
            }
        }

        composeLog.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                RetroTheme {
                    LogLayout(
                        modifier = Modifier
                            .padding(
                                horizontal = LocalSpacing.current.normal,
                            )
                    )
                }
            }
        }
    }

    private fun renderView(state: MainView.State) {
        state.apply {
            if (shouldKeepScreenOn) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            requestForPermissions.ghost?.let { makePermissionsRequest(it) }
            requestForScreenCapture.ghost?.let { makeScreenCaptureRequest() }
            composeLog.isInvisible = !this.isLogViewEnabled
        }
    }

    private fun makeScreenCaptureRequest() {
        @Suppress("DEPRECATED_METHOD")
        startActivityForResult(
            mediaProjectionManager.createScreenCaptureIntent(),
            MEDIA_PROJECTION_REQUEST_CODE
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createPermissionsMap(): () -> Map<String, PermissionOptionalDetails> = {
        listOf(
            Pair(
                Manifest.permission.RECORD_AUDIO,
                PermissionRequiredDetails(
                    getString(R.string.record_permission_title),
                    getString(R.string.record_permission_message),
                    getString(R.string.record_required_message)
                )
            )
        ).toMap()
    }

    private fun makePermissionsRequest(requestForPermissions: Set<String>) {
        val permissionRequests = HashMap<String, PermissionOptionalDetails?>()
        for (permissionName in requestForPermissions) {
            permissionRequests[permissionName] = permissionsMap()[permissionName]
        }
        permissionsManager.addPermissions(permissionRequests)
        permissionsManager.makePermissionRequest(true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MEDIA_PROJECTION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val projection = mediaProjectionManager.getMediaProjection(resultCode, data)
                viewModel.input.onNext(MainView.Input.MediaProjectionUpdated(projection))
                projection.registerCallback(object : MediaProjection.Callback() {
                    override fun onStop() {
                        viewModel.input.onNext(MainView.Input.MediaProjectionUpdated(null))
                    }
                }, Handler(Looper.myLooper()!!))
            } else {
                viewModel.input.onNext(MainView.Input.MediaProjectionUpdated(null))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}

private const val MEDIA_PROJECTION_REQUEST_CODE = 22
