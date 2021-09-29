package com.omar.retromp3recorder.app.ui.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.uiutils.observe

class SettingsActivity : AppCompatActivity(R.layout.activity_settings) {
    private val toolbar: Toolbar
        get() = findViewById(R.id.toolbar)

    private val viewModel by viewModels<SettingsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.state.observe(this, ::renderState)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed(); true
            }
            else -> false
        }
    }

    private fun renderState(state: SettingsView.State) {}
}