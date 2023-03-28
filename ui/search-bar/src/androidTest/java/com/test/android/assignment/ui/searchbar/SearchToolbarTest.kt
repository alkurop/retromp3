package com.test.android.assignment.ui.searchbar

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchToolbarTest {

    @get: Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val clearContentDescription = "clearContentDescription"

    @OptIn(ExperimentalMaterial3Api::class)
    private fun setUp(
        hint: String,
        onSubmit: (String) -> Unit = {},
    ) {
        composeTestRule.setContent {
            SearchBarLayout(
                state = rememberSearchBarInputState(hint = hint),
                onSubmit = {
                    onSubmit(it)
                },
                content = {
                    Text(text = "I'm content")
                },
                clearContentDescription = clearContentDescription,
            )
        }
    }


    @Test
    fun allViewsShouldExist() {
        val hint = "hintString"
        setUp(hint = hint)

        composeTestRule.apply {
            onNodeWithText(hint).assertIsDisplayed()
            onNodeWithContentDescription(clearContentDescription).assertDoesNotExist()
        }
    }

    @Test
    fun hintHidesOnInput() {
        val hint = "hintString"
        val input = "some input"

        setUp(hint = hint)

        composeTestRule.apply {
            onNodeWithText(hint).performClick()

            onNodeWithText("").apply {
                performTextInput(input)
            }

            onNodeWithText(input).assertIsDisplayed()
            onNodeWithText(hint).assertDoesNotExist()
        }
    }


    @Test
    fun clearButtonAppearsOnInput() {
        val hint = "hintString"
        val input = "some input"
        setUp(hint = hint)

        composeTestRule.apply {
            composeTestRule.onNodeWithText(hint).performClick()
            onNodeWithText("").apply {
                performTextInput(input)
            }

            onNodeWithContentDescription(clearContentDescription).assertExists()
        }
    }

    @Test
    fun clickClearCleansTextInput() {
        val hint = "hintString"
        val input = "some input"
        setUp(hint = hint)

        composeTestRule.apply {
            onNodeWithText(hint).performClick()
            onNodeWithText("").apply {
                performTextInput(input)
            }
            onNodeWithContentDescription(clearContentDescription).performClick()
            composeTestRule.onNodeWithText(input).assertDoesNotExist()
        }
    }

    @Test
    fun clickClearSubmitsEmptyQuery() {
        val hint = "hintString"
        var isSubmitted = false

        var input = "some input"
        val submit: (String) -> Unit = {
            isSubmitted = true
            input = it
        }

        setUp(hint = hint, onSubmit = submit)

        composeTestRule.apply {
            onNodeWithText(hint).performClick()
            onNodeWithText("").apply {
                performTextInput(input)
            }
            onNodeWithContentDescription(clearContentDescription).performClick()
        }

        assertEquals(isSubmitted, true)
        assertEquals(input, "")
    }

    @Test
    fun inputUpdatesInputState() {
        val newInput = "some input"

        var resultInput = "some input"
        val submit: (String) -> Unit = {
            resultInput = it
        }

        val hint = "hintString"

        setUp(hint = hint, onSubmit = submit)

        composeTestRule.onNodeWithText(hint).performClick()
        composeTestRule.onNodeWithText("").apply {
            performTextInput(newInput)
        }
        assertEquals(resultInput, newInput)
    }


    @Test
    fun submitTriggeredOnImeAction() {
        val hint = "hintString"
        val newInput = "some input"

        var isSubmitted = false
        val submit: (String) -> Unit = {
            isSubmitted = true
        }
        setUp(hint = hint, onSubmit = submit)

        composeTestRule.apply {
            onNodeWithText(hint).performClick()
            onNodeWithText("").apply {
                performTextInput(newInput)
            }
            onNodeWithText(newInput).performImeAction()
        }
        assertEquals(isSubmitted, true)
    }
}
