package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.database.DbUpdaterUCSuspend
import com.omar.retromp3recorder.bl.waveform.WaveformScannerSuspend
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.isEmpty
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WaveformScanUpdaterUCSuspend @Inject constructor(
    private val dbUpdaterUC: DbUpdaterUCSuspend,
    private val waveformScanner: WaveformScannerSuspend,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(input: List<ExistingFileWrapper>): List<ExistingFileWrapper> {
        scopeJobWrapper.cancelAndJoin()
        val batch = input.filter { it.wavetable.isEmpty() }
        return if (batch.isEmpty()) {
            input
        } else {
            withContext(scopeJobWrapper.coroutineContext) {
                val result = batch.map { existingFileWrapper ->
                    waveformScanner.execute(existingFileWrapper)
                }
                dbUpdaterUC.execute(result)
                result
            }
        }
    }
}
