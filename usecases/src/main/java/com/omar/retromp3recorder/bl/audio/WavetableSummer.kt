package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.dto.Wavetable
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder

data class WavetableSummer(
    var list: MutableList<Byte> = mutableListOf(),
    private var denomitator: Int = 1,
    private val buffer: MutableList<Byte> = mutableListOf()
) {
    var increment: Long = 0
    private fun add(item: Byte) {
        increment++
        if (list.size >= MAX_SIZE) {
            denomitator *= 2
            list = list.windowed(2, 2, false) { it.maxOrNull() ?: 0 }.toMutableList()
        }
        if (buffer.size <= denomitator) {
            buffer.add(item)
        } else {
            val element = buffer.maxOrNull() ?: 0
            list.add(element)
            buffer.clear()
            buffer.add(item)
        }

    }

    fun getProgress(): Long {
        return increment
    }

    //append buffer only for final waveform
    fun toWaveTable(
        sampleRate: Mp3VoiceRecorder.WaveTableSampleRate,
        appendBuffer: Boolean = true
    ): Wavetable {
        if (appendBuffer) list.add(buffer.average().toInt().toByte())
        val byteArray = list.toByteArray()
        return Wavetable(byteArray, sampleRate.value * denomitator)
    }

    companion object {
        private const val MAX_SIZE = 1024

        val recordCollectFunction: (WavetableSummer, Byte) -> Unit = { summer, byte ->
            summer.add(byte)
        }

        val displayScanFunction: (WavetableSummer, Byte) -> WavetableSummer = { summer, byte ->
            summer.add(byte)
            summer
        }
    }
}

