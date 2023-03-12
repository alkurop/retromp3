package com.omar.retromp3recorder.app.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class MainLayoutTest {
    @get: Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()


    @Test
    fun someTest(){
        assertThat(1L).isEqualTo(1)
    }
}
