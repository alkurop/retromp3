package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.file

import app.cash.turbine.test
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.Optional
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RenameFileMenuStateMapperFlowTest {
    private val audioStateMapper = mockk<AudioStateMapper>()
    private lateinit var repo: CurrentFileRepo
    private lateinit var tested: RenameFileMenuStateMapperFlow

    @Before
    fun setUp() {
        repo = CurrentFileRepo()
        tested = RenameFileMenuStateMapperFlow(audioStateMapper, repo)
    }

    @Test
    fun `WHEN audio state idle and has current file Then enabled`() = runTest {
        repo.emit(Optional(MockFileFactory.giveFile()))
        every { audioStateMapper.observe() } returns Observable.just(AudioState.Idle)

        tested.flow().test {
            val item = awaitItem()[0]
            val popup = item as MenuContract.Item.Popup
            assertEquals(MenuPopup.Rename, popup.menuPopup)
            assert(popup.isEnabled)
        }
    }

    @Test
    fun `WHEN audio state idle and NOT has current file Then disabled`() = runTest {
        repo.emit(Optional.empty())
        every { audioStateMapper.observe() } returns Observable.just(AudioState.Idle)

        tested.flow().test {
            val item = awaitItem()[0]
            assert((item as MenuContract.Item.Popup).isEnabled.not())
        }
    }

    @Test
    fun `WHEN audio state Playing THEN file Then disabled`() = runTest {
        repo.emit(Optional(MockFileFactory.giveFile()))
        every { audioStateMapper.observe() } returns Observable.just(AudioState.Playing)

        tested.flow().test {
            val item = awaitItem()[0]
            assert((item as MenuContract.Item.Popup).isEnabled.not())
        }
    }

    @Test
    fun `WHEN audio state Seek_Paused THEN file Then disabled`() = runTest {
        repo.emit(Optional(MockFileFactory.giveFile()))
        every { audioStateMapper.observe() } returns Observable.just(AudioState.Seek_Paused)

        tested.flow().test {
            val item = awaitItem()[0]
            assert((item as MenuContract.Item.Popup).isEnabled.not())
        }
    }

    @Test
    fun `WHEN audio state Recording THEN file Then disabled`() = runTest {
        repo.emit(Optional(MockFileFactory.giveFile()))
        every { audioStateMapper.observe() } returns Observable.just(AudioState.Recording)

        tested.flow().test {
            val item = awaitItem()[0]
            assert((item as MenuContract.Item.Popup).isEnabled.not())
        }
    }
}
