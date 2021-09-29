package com.omar.retromp3recorder.app.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.omar.retromp3recorder.app.BuildConfig
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.storage.repo.FeatureLevel

class SettingsActivity : AppCompatActivity(R.layout.activity_settings) {
    private val toolbar: Toolbar
        get() = findViewById(R.id.toolbar)
    private val prodContainer: LinearLayout
        get() = findViewById(R.id.prod_container)
    private val expContainer: LinearLayout
        get() = findViewById(R.id.experimental_container)
    private val debugContainer: LinearLayout
        get() = findViewById(R.id.debug_container)
    private val debugSection: LinearLayout
        get() = findViewById(R.id.debug_section)

    private val viewModel by viewModels<SettingsViewModel>()
    private val margin by lazy { resources.getDimensionPixelSize(R.dimen.setting_checkbox_margin) }

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

    @SuppressLint("InflateParams")
    private fun renderState(state: SettingsView.State) {
        state.featureFlagsCollection?.let { featureFlagsCollection ->
            debugContainer.removeAllViews()
            expContainer.removeAllViews()
            prodContainer.removeAllViews()
            featureFlagsCollection.featuresMap.forEach { (flag, setting) ->
                val isEnabled = setting.isEnabled(flag)
                val container = when (flag.featureLevel) {
                    FeatureLevel.Debug -> debugContainer
                    FeatureLevel.Experimental -> expContainer
                    FeatureLevel.Production -> prodContainer
                }
                val view = LayoutInflater.from(this).inflate(
                    R.layout.setting_checkbox, null
                ) as CheckBox
                container.addView(view)
                view.isChecked = isEnabled
                view.setText(flag.friendlyName)
                val lp = view.layoutParams as LinearLayout.LayoutParams
                lp.setMargins(margin,margin,margin,margin)
                view.layoutParams = lp
                view.setOnCheckedChangeListener { _, state -> }
            }
            debugSection.isVisible = BuildConfig.DEBUG
        }
    }
}