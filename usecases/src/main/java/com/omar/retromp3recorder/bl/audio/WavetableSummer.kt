package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.dto.Wavetable
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder

data class WavetableSummer(
    private var list: MutableList<Byte> = mutableListOf(),
    private var denomitator: Int = 1,
    private val buffer: MutableList<Byte> = mutableListOf()
) {
    private fun add(item: Byte) {
        if (list.size == MAX_SIZE) {
            denomitator *= 2
            list = list.windowed(2, 2, false) { it.maxOrNull() ?: 0 }.toMutableList()
        }
        if (buffer.size < denomitator) {
            buffer.add(item)
        } else {
            val element = buffer.average().toInt().toByte()
            list.add(element)
            buffer.clear()
            buffer.add(item)
        }
    }

    private fun toByteArray(): ByteArray {
        list.add(buffer.average().toInt().toByte())
        return list.toByteArray()
    }

    fun toWaveTable(sampleRate: Mp3VoiceRecorder.WaveTableSampleRate): Wavetable {
        return Wavetable(toByteArray(), sampleRate.value * denomitator)
    }

    companion object {
        private const val MAX_SIZE = 1024

        val collectFunction: (WavetableSummer, Byte) -> Unit = { summer, byte ->
            summer.add(byte)
        }

        val scanFunction: (WavetableSummer, Byte) -> WavetableSummer = { summer, byte ->
            summer.add(byte)
            summer
        }
    }
}

