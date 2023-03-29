package com.omar.retromp3recorder.domain

data class Wavetable(
    val bytes: ByteArray,
    val stepMillis: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Wavetable

        if (!bytes.contentEquals(other.bytes)) return false
        if (stepMillis != other.stepMillis) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + stepMillis
        return result
    }
}

fun ByteArray?.isEmpty(): Boolean = this?.firstOrNull { it != 0.toByte() } == null
