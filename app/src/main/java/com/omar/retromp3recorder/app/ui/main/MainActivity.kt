package com.omar.retromp3recorder.app.ui.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.github.alkurop.jpermissionmanager.PermissionOptionalDetails
import com.github.alkurop.jpermissionmanager.PermissionRequiredDetails
import com.github.alkurop.jpermissionmanager.PermissionsManager
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.settings.SettingsActivity
import com.omar.retromp3recorder.app.uiutils.observe
import java.util.*

class MainActivity : AppCompatActivity() {
    private val permissionsManager: PermissionsManager by lazy { PermissionsManager(this) }
    private val permissionsMap: Map<String, PermissionOptionalDetails> by lazy { createPermissionsMap() }
    private val viewModel by viewModels<MainViewModel>()
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val logFragment by lazy { findViewById<View>(R.id.log_fragment) }
    private val mediaProjectionManager by lazy { getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager }
    private var isContentViewSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.observe(this, ::renderView)

    }

    private fun renderView(state: MainView.State) {
        state.apply {
            if (shouldRestart) {
                finish()
                startActivity(Intent(this@MainActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                Toast.makeText(this@MainActivity, R.string.rerendered, Toast.LENGTH_SHORT).show()
            }
            // needed in case activity is restarted outside of the render cycle
            if (!isContentViewSet) {
                isContentViewSet = true
                if (isNewLayout) {
                    setContentView(R.layout.activity_main_new)
                } else {
                    setContentView(R.layout.activity_main)
                }
            }
            if (actionBar == null) {
                setSupportActionBar(toolbar)
            }

            if (shouldKeepScreenOn) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            requestForPermissions.ghost?.let { makePermissionsRequest(it) }
            requestForScreenCapture.ghost?.let { makeScreenCaptureRequest() }
            logFragment.isVisible = this.isLogViewEnabled
        }
    }

    private fun makeScreenCaptureRequest() {
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

    private fun createPermissionsMap(): Map<String, PermissionOptionalDetails> =
        listOf(
            Pair(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                PermissionRequiredDetails(
                    getString(R.string.write_permission_title),
                    getString(R.string.write_permission_message),
                    getString(R.string.write_required_message)
                )
            ),
            Pair(
                Manifest.permission.RECORD_AUDIO,
                PermissionRequiredDetails(
                    getString(R.string.record_permission_title),
                    getString(R.string.record_permission_message),
                    getString(R.string.record_required_message)
                )
            )
        ).toMap()

    private fun makePermissionsRequest(requestForPermissions: Set<String>) {
        val permissionRequests = HashMap<String, PermissionOptionalDetails?>()
        for (permissionName in requestForPermissions) {
            permissionRequests[permissionName] = permissionsMap[permissionName]
        }
        permissionsManager.addPermissions(permissionRequests)
        permissionsManager.makePermissionRequest(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MEDIA_PROJECTION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val projection = mediaProjectionManager.getMediaProjection(resultCode, data)
                viewModel.input.onNext(MainView.Input.MediaProjectionUpdated(projection))
                Toast.makeText(this, getString(R.string.projection_acquired), Toast.LENGTH_LONG)
                    .show()
                projection.registerCallback(object : MediaProjection.Callback() {
                    override fun onStop() {
                        viewModel.input.onNext(MainView.Input.MediaProjectionUpdated(null))
                    }
                }, Handler())
            } else {
                Toast.makeText(this, getString(R.string.projection_not_acquired), Toast.LENGTH_LONG)
                    .show()
                viewModel.input.onNext(MainView.Input.MediaProjectionUpdated(null))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}

private const val MEDIA_PROJECTION_REQUEST_CODE = 22
