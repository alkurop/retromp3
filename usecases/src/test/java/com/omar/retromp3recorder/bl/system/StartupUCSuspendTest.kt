package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.files.NewCurrentFileUpdaterSuspend
import com.omar.retromp3recorder.bl.files.TakeLastFileDirScanUCSuspend
import com.omar.retromp3recorder.bl.settings.FeatureMapLoadUCSuspend
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUCSuspend
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StartupUCSuspendTest {
    @MockK
    lateinit var takeLastFileWithScanDirScanUC: TakeLastFileDirScanUCSuspend

    @MockK
    lateinit var newCurrentFileUpdaterSuspend: NewCurrentFileUpdaterSuspend

    @MockK
    lateinit var loadRecorderSettingsUCSuspend: LoadRecorderSettingsUCSuspend

    @MockK
    lateinit var featureMapLoadUCSuspend: FeatureMapLoadUCSuspend
    private val dispatcher = UnconfinedTestDispatcher()
    private val jobWrapper = ScopeJobWrapper(dispatcher)

    private lateinit var startupUCSuspend: StartupUCSuspend

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        startupUCSuspend = StartupUCSuspend(
            takeLastFileWithScanDirScanUC,
            newCurrentFileUpdaterSuspend,
            loadRecorderSettingsUCSuspend,
            featureMapLoadUCSuspend,
            jobWrapper
        )
    }

    @Test
    fun stub() = fail()
}
