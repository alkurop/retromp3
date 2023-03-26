package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.database.DbUpdaterUCSuspend
import com.omar.retromp3recorder.bl.files.FileRepoUpdaterUCSuspend
import com.omar.retromp3recorder.bl.waveform.WaveformScannerSuspend
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.isEmpty
import com.omar.retromp3recorder.utils.domain.AmplitudaDealer
import com.omar.retromp3recorder.utils.platform.ScopeJobWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class WaveformScanUpdaterUCSuspend @Inject constructor(
    private val amplitudaDealer: AmplitudaDealer,
    private val dbUpdaterUC: DbUpdaterUCSuspend,
    private val fileRepoUpdaterUC: FileRepoUpdaterUCSuspend,
    private val waveformScanner: WaveformScannerSuspend,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(input: List<ExistingFileWrapper>) {
        scopeJobWrapper.cancelAndJoin()
        val batch = input.filter { it.wavetable.isEmpty() }
        if (batch.isEmpty()) return

        scopeJobWrapper.launch {
            batch.forEach { existingFileWrapper ->
                val result = waveformScanner
                    .execute(
                        existingFileWrapper,
                        amplitudaDealer
                    )
                dbUpdaterUC.execute(listOf(result))
                fileRepoUpdaterUC.execute(listOf(result))
            }
        }
    }
}
