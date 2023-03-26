package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic

import app.cash.turbine.test
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.merged.FileActionsStateMapperFlow
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.domain.VisibilityEnabler
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import com.omar.retromp3recorder.utils.domain.toOptional
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MenuStateExcavatorFlowTest {
    private lateinit var playerControlsRepo: PlayerControlsRepo
    private val audioStateMapper = mockk<AudioStateMapper>(relaxed = true)
    private lateinit var currentFileRepo: CurrentFileRepo
    private val fileActionsStateMapper = mockk<FileActionsStateMapperFlow>()
    private lateinit var tested: MenuStateExcavatorFlow

    @Before
    fun setUp() {
        every { audioStateMapper.flow() } returns flowOf(AudioState.Playing)
        coEvery { fileActionsStateMapper.flow() } returns flowOf(
            listOf(
                MenuContract.Item.Popup(
                    menuPopup = MenuPopup.Rename,
                    isEnabled = true
                ),
                MenuContract.Item.Popup(
                    menuPopup = MenuPopup.Delete,
                    isEnabled = true
                )
            )
        )
        playerControlsRepo = PlayerControlsRepo()
        currentFileRepo = CurrentFileRepo()
        tested = MenuStateExcavatorFlow(
            playerControlsRepo,
            audioStateMapper,
            currentFileRepo,
            fileActionsStateMapper
        )
    }

    @Test
    fun `Result popup list has RangeBar,Crop,Search,Delete,Rename popups`() = runTest {
        tested.flow().test {
            val item = awaitItem()
            val popupItems = item.items.mapNotNull { it as? MenuContract.Item.Popup }
            val enableItems = item.items.mapNotNull { it as? MenuContract.Item.Enable }
            assertNotNull(enableItems.firstOrNull { it.enabler == VisibilityEnabler.RangeBar })
            assertNotNull(popupItems.firstOrNull { it.menuPopup == MenuPopup.Crop })
            assertNotNull(popupItems.firstOrNull { it.menuPopup == MenuPopup.Search })
            assertNotNull(popupItems.firstOrNull { it.menuPopup == MenuPopup.Delete })
            assertNotNull(popupItems.firstOrNull { it.menuPopup == MenuPopup.Rename })
        }
    }

    @Test
    fun `WHEN audio state Recording then flag isVisible false`() = runTest {
        every { audioStateMapper.flow() } returns flowOf(AudioState.Recording)
        tested.flow().test {
            val item = awaitItem()
            assert(item.isVisible.not())
        }
    }

    @Test
    fun `WHEN audio state Playing THEN flag isVisible true`() = runTest {
        every { audioStateMapper.flow() } returns flowOf(AudioState.Playing)
        tested.flow().test {
            val item = awaitItem()
            assert(item.isVisible)
        }
    }


    @Test
    fun `WHEN audio state Idle THEN flag isVisible true`() = runTest {
        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)
        tested.flow().test {
            val item = awaitItem()
            assert(item.isVisible)
        }
    }

    @Test
    fun `WHEN no file THEN Search popup disabled`() = runTest {
        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)
        tested.flow().test {
            val item = awaitItem()
            val popupItems = item.items.mapNotNull { it as? MenuContract.Item.Popup }
            val searchItem = popupItems.first { it.menuPopup == MenuPopup.Search }
            assert(searchItem.isEnabled.not())
        }
    }

    @Test
    fun `WHEN has file THEN Search popup enabled`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveExistingFile().toOptional())
        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)
        tested.flow().test {
            val item = awaitItem()
            val popupItems = item.items.mapNotNull { it as? MenuContract.Item.Popup }
            val searchItem = popupItems.first { it.menuPopup == MenuPopup.Search }
            assert(searchItem.isEnabled)
        }
    }

    @Test
    fun `WHEN range visible THEN Crop,RangeBar popup enabled`() = runTest {
        playerControlsRepo.emit(PlayerControls(rangeSettings = PlayerControls.RangeSettings(isVisible = true)))

        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)
        tested.flow().test {
            val item = awaitItem()
            val popupItems = item.items.mapNotNull { it as? MenuContract.Item.Popup }
            val enablerItems = item.items.mapNotNull { it as? MenuContract.Item.Enable }

            val cropItem = popupItems.first { it.menuPopup in listOf(MenuPopup.Crop) }
            val rangeItem = enablerItems.first { it.enabler in listOf(VisibilityEnabler.RangeBar) }

            assert(cropItem.isEnabled)
            assert(rangeItem.isEnabled)
        }
    }

    @Test
    fun `WHEN range NOT visible THEN Crop,RangeBar popup NOT enabled`() = runTest {

        playerControlsRepo.emit(PlayerControls(rangeSettings = PlayerControls.RangeSettings(isVisible = false)))

        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)
        tested.flow().test {
            val item = awaitItem()
            val popupItems = item.items.mapNotNull { it as? MenuContract.Item.Popup }
            val enablerItems = item.items.mapNotNull { it as? MenuContract.Item.Enable }

            val cropItem = popupItems.first { it.menuPopup in listOf(MenuPopup.Crop) }
            val rangeItem = enablerItems.first { it.enabler in listOf(VisibilityEnabler.RangeBar) }

            assert(cropItem.isEnabled.not())
            assert(rangeItem.isEnabled.not())
        }
    }


}

