package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.domain.Wavetable

class WavetableSummer(
    private var list: MutableList<Byte> = mutableListOf(),
    private var denominator: Int = 1,
    private val buffer: MutableList<Byte> = mutableListOf(),
    private var increment: Long = 0,
    private val timestamp: Long = System.currentTimeMillis(),
    private var lengthMillis: Long = 0
) {

    fun getLength(): Long = lengthMillis

    private fun add(item: Byte) {
        lengthMillis = System.currentTimeMillis() - timestamp
        increment++
        if (list.size >= MAX_WAVEFORM_SIZE) {
            denominator *= 2
            list = list.windowed(2, 2, false) {
                it.maxOrNull() ?: 0
            }.toMutableList()
        }
        if (buffer.size < denominator) {
            buffer.add(item)
        } else {
            val element = buffer.maxOrNull() ?: 0
            list.add(element)
            buffer.clear()
            buffer.add(item)
        }
    }

    //append buffer only for final waveform
    fun toWaveTable(
        appendBuffer: Boolean = true
    ): Wavetable {
        if (appendBuffer) list.add(buffer.average().toInt().toByte())
        val byteArray = list.toByteArray()
        return Wavetable(byteArray, byteArray.size * denominator)
    }

    companion object {
        const val MAX_WAVEFORM_SIZE = 1000

        val reducer: (WavetableSummer, Byte) -> WavetableSummer = { summer, byte ->
            summer.add(byte)
            summer
        }
    }
}

