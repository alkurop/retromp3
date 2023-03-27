package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.database.DbUpdaterUCSuspend
import com.omar.retromp3recorder.bl.waveform.WaveformScannerSuspend
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.isEmpty
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.platform.AmplitudaDealer
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WaveformScanUpdaterUCSuspend @Inject constructor(
    private val amplitudaDealer: AmplitudaDealer,
    private val dbUpdaterUC: DbUpdaterUCSuspend,
    private val waveformScanner: WaveformScannerSuspend,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(input: List<ExistingFileWrapper>): List<ExistingFileWrapper> {
        scopeJobWrapper.cancelAndJoin()
        val batch = input.filter { it.wavetable.isEmpty() }
        if (batch.isEmpty()) return input

        return withContext(scopeJobWrapper.coroutineContext) {
            val result = batch.map { existingFileWrapper ->
                waveformScanner
                    .execute(
                        existingFileWrapper,
                        amplitudaDealer
                    )
            }
            dbUpdaterUC.execute(result)
            result
        }
    }
}
