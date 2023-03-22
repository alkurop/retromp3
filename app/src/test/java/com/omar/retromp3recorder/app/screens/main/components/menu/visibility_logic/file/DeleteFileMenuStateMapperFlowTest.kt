package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.file

import app.cash.turbine.test
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.Optional
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteFileMenuStateMapperFlowTest {
    private val audioStateMapper = mockk<AudioStateMapper>()
    private lateinit var repo: CurrentFileRepo
    private lateinit var tested: DeleteFileMenuStateMapperFlow

    @Before
    fun setUp() {
        repo = CurrentFileRepo()
        tested = DeleteFileMenuStateMapperFlow(audioStateMapper, repo)
    }

    @Test
    fun `WHEN audio state idle and has current file Then enabled`() = runTest {
        repo.emit(Optional(MockFileFactory.giveExistingFile()))
        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)

        tested.flow().test {
            val item = awaitItem()[0]
            val popup = item as MenuContract.Item.Popup

            Assert.assertEquals(MenuPopup.Delete, popup.menuPopup)
            assert(popup.isEnabled)
        }
    }

    @Test
    fun `WHEN audio state idle and NOT has current file Then disabled`() = runTest {
        repo.emit(Optional.empty())
        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)

        tested.flow().test {
            val item = awaitItem()[0]
            assert((item as MenuContract.Item.Popup).isEnabled.not())
        }
    }

    @Test
    fun `WHEN audio state Playing THEN file Then disabled`() = runTest {
        repo.emit(Optional(MockFileFactory.giveExistingFile()))
        every { audioStateMapper.flow() } returns flowOf(AudioState.Playing)

        tested.flow().test {
            val item = awaitItem()[0]
            assert((item as MenuContract.Item.Popup).isEnabled.not())
        }
    }

    @Test
    fun `WHEN audio state Seek_Paused THEN file Then disabled`() = runTest {
        repo.emit(Optional(MockFileFactory.giveExistingFile()))
        every { audioStateMapper.flow() } returns flowOf(AudioState.SeekPaused)

        tested.flow().test {
            val item = awaitItem()[0]
            assert((item as MenuContract.Item.Popup).isEnabled.not())
        }
    }

    @Test
    fun `WHEN audio state Recording THEN file Then disabled`() = runTest {
        repo.emit(Optional(MockFileFactory.giveExistingFile()))
        every { audioStateMapper.flow() } returns flowOf(AudioState.Recording)

        tested.flow().test {
            val item = awaitItem()[0]
            assert((item as MenuContract.Item.Popup).isEnabled.not())
        }
    }
}

