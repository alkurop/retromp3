package com.omar.retromp3recorder.app.test


import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.main.MainActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test


class RecordTest {

    @get:Rule
    val permissionsRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.RECORD_AUDIO
    )

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun recordSmokeTest() {
        onView(withId(R.id.acf_stop)).check(matches(isNotEnabled()))
        onView(withId(R.id.acf_record)).perform(ViewActions.click())
        onView(withId(R.id.acf_stop)).perform(ViewActions.click())
        onView(withId(R.id.current_file_text)).check(matches(not(withText(R.string.please_record_something))))
    }
}
