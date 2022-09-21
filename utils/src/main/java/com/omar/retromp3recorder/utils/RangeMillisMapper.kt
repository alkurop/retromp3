package com.omar.retromp3recorder.utils

import com.omar.retromp3recorder.dto.FromToMillis
import com.omar.retromp3recorder.dto.FromToMicro
import com.omar.retromp3recorder.dto.PlayerRange

fun PlayerRange.toFromToMillis(durationMillis: Long): FromToMillis {
    val rangeMultiplier = if (this.max == 0) 1 else durationMillis / this.max
    val fromMillis = if (this.from == 0) 0 else this.from * rangeMultiplier
    val toMillis = if (this.to == 0) 0 else this.to * rangeMultiplier
    return FromToMillis(fromMillis, toMillis)
}

fun FromToMillis.toMicro() = FromToMicro(this.from * 1000, this.to * 1000)
