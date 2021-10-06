package com.omar.retromp3recorder.app.test


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.omar.retromp3recorder.app.ui.settings.SettingsActivity
import com.omar.retromp3recorder.app.R
import org.junit.Rule
import org.junit.Test


class SettingsTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SettingsActivity::class.java)

    @Test
    fun mic_audio_source_is_default() {
        onView(withText(R.string.rcdr_mic)).check(matches(isChecked()))
    }
}