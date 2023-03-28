package com.test.assignment.ui.paging

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.asFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class PagingColumnViewKtTest {

    @get: Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val errorText = "error"
    private val loadingText = "loading"
    private val emptyText = "empty"
    private val itemText = "item"

    private fun setUp(data: List<PagingData<String>>) {
        composeTestRule.setContent {
            val items = data.asFlow().collectAsLazyPagingItems()
            PagingColumnView(
                data = items,
                emptyView = { Text(emptyText) },
                errorView = { Text(errorText) },
                loadingView = { Text(loadingText) },
                itemBuilder = { _, item -> Text(text = item) },
                idProvider = { it }
            )
        }
    }


    @Test
    fun whenIsRefreshingDataEmptyLoadingDisplayed() {
        val pagingData = listOf(
            PagingData.from(
                listOf<String>(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Loading,
                    prepend = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false),
                ),
            )
        )
        setUp(pagingData)

        composeTestRule.apply {
            onNodeWithText(loadingText).assertIsDisplayed()
        }
    }

    @Test
    fun whenErrorDataEmptyErrorDisplayed() {
        val pagingData = listOf(
            PagingData.from(
                listOf<String>(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Error(Error("expected")),
                    prepend = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false),
                ),
            )
        )
        setUp(pagingData)

        composeTestRule.apply {
            onNodeWithText(errorText).assertIsDisplayed()
        }
    }

    @Test
    fun whenEmptyDataEmptyDisplayed() {
        val pagingData = listOf(
            PagingData.from(
                listOf<String>(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(true),
                    prepend = LoadState.NotLoading(true),
                    append = LoadState.NotLoading(true),
                ),
            )
        )
        setUp(pagingData)

        composeTestRule.apply {
            onNodeWithText(emptyText).assertIsDisplayed()
        }
    }

    @Test
    fun whenNotEmptyDataDisplayed() {
        val pagingData = listOf(
            PagingData.from(
                listOf(itemText),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(true),
                    prepend = LoadState.NotLoading(true),
                    append = LoadState.NotLoading(true),
                ),
            )
        )
        setUp(pagingData)

        composeTestRule.apply {
            onNodeWithText(itemText).assertIsDisplayed()
        }
    }

    @Test
    fun whenNotEmptyAppendLoadingNoLoadingDisplayed() {
        val pagingData = listOf(
            PagingData.from(
                listOf(itemText),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Loading,
                    prepend = LoadState.NotLoading(true),
                    append = LoadState.NotLoading(true),
                ),
            )
        )
        setUp(pagingData)

        composeTestRule.apply {
            onNodeWithText(itemText).assertIsDisplayed()
            onNodeWithText(loadingText).assertDoesNotExist()
        }
    }

    @Test
    fun whenNotEmptyLoadingNoLoadingDisplayed() {
        val pagingData = listOf(
            PagingData.from(
                listOf(itemText),
                sourceLoadStates = LoadStates(
                    append = LoadState.Loading,
                    refresh = LoadState.NotLoading(true),
                    prepend = LoadState.NotLoading(true),
                ),
            )
        )
        setUp(pagingData)

        composeTestRule.apply {
            onNodeWithText(itemText).assertIsDisplayed()
            onNodeWithText(loadingText).assertDoesNotExist()
        }
    }

    @Test
    fun whenNotEmptyErrorNotErrorDisplayed() {
        val pagingData = listOf(
            PagingData.from(
                listOf(itemText),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Error(Error("Expected")),
                    prepend = LoadState.NotLoading(true),
                    append = LoadState.NotLoading(true),
                ),
            )
        )
        setUp(pagingData)

        composeTestRule.apply {
            onNodeWithText(itemText).assertIsDisplayed()
            onNodeWithText(loadingText).assertDoesNotExist()
        }
    }
}
