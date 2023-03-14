package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.waveform.WaveformScanner
import com.omar.retromp3recorder.data.mock.MockCropRequestFactory
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockSuggestionFactory
import com.omar.retromp3recorder.data.mock.MockTagsFactory
import com.omar.retromp3recorder.domain.CropResponse
import com.omar.retromp3recorder.io.audiotransformer.AudioCropper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.utils.domain.AmplitudaDealer
import com.omar.retromp3recorder.utils.domain.FileLister
import com.omar.retromp3recorder.utils.domain.Mp3TagsEditor
import io.mockk.*
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CropUCTest {
    private val amplitudaDealer = mockk<AmplitudaDealer>()
    private val audioCropper = mockk<AudioCropper>(relaxed = true)
    private val fileLister = mockk<FileLister>(relaxed = true)
    private val mp3TagsEditor = mockk<Mp3TagsEditor>(relaxed = true)
    private val gatherCropRequestUC = mockk<GatherCropRequestUC>()

    private val waveformScanner = mockk<WaveformScanner>(relaxed = true)
    private val appDatabase = mockk<AppDatabase>()
    private val dao = mockk<FileDbEntityDao>(relaxed = true)

    private val mockFile = MockFileFactory.giveFile()
    private val mockTags = MockTagsFactory.giveTags()
    private val mockCropRequest = MockCropRequestFactory.giveCropRequest()
    private val mockSuggestion = MockSuggestionFactory.giveTestSuggestion()

    private lateinit var tested: CropUC

    @Before
    fun setUp() {
        every { appDatabase.fileEntityDao() } returns dao
        every { fileLister.discoverFile(any()) } returns mockFile
        every { mp3TagsEditor.getTags(any()) } returns mockTags
        coEvery { gatherCropRequestUC.execute(any()) } returns mockCropRequest
        every { waveformScanner.execute(any(), any()) } returns Single.just(mockFile)

        tested = CropUC(
            amplitudaDealer,
            appDatabase,
            audioCropper,
            gatherCropRequestUC,
            fileLister,
            mp3TagsEditor,
            waveformScanner
        )
    }

    @Test
    fun `WHEN crop failed THEN empty result`() = runTest {
        every { audioCropper.crop(any()) } returns CropResponse(false)
        val result = tested.execute(mockSuggestion)

        assertNull(result.value)
        verify(exactly = 0) { mp3TagsEditor.getTags(any()) }
        verify(exactly = 0) { mp3TagsEditor.setTags(any(), any()) }
        verify(exactly = 0) { fileLister.discoverFile(any()) }
        verify(exactly = 0) { waveformScanner.execute(any(), any()) }
        verify(exactly = 0) { dao.insert(any()) }
    }

    @Test
    fun `WHEN crop success THEN sequence executed`() = runTest {
        every { audioCropper.crop(any()) } returns CropResponse(true)
        val result = tested.execute(mockSuggestion)

        assertNotNull(result.value)
        verifyOrder {
            mp3TagsEditor.getTags(any())
            mp3TagsEditor.setTags(any(), any())
            fileLister.discoverFile(any())
            waveformScanner.execute(any(), any())
            dao.insert(any())
        }
    }

    @Test
    fun `WHEN crop success THEN mp3 tags update title`() = runTest {
        every { audioCropper.crop(any()) } returns CropResponse(true)
        tested.execute(mockSuggestion)

        verify {
            mp3TagsEditor.setTags(any(), mockTags.copy(title = mockSuggestion.name))
        }
    }
}
