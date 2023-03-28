package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.bl.waveform.RecordWavetableMapper
import com.omar.retromp3recorder.bl.waveform.WavetableSummer
import com.omar.retromp3recorder.domain.Wavetable
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

class CollectWavetableUC @Inject constructor(
    private val recorderMapper: RecordWavetableMapper,
) {
    suspend fun execute(): Wavetable {
        return recorderMapper.flow()
            .scan(WavetableSummer(), WavetableSummer.reducer)
            .map { it.toWaveTable() }
            .last()
    }
}
