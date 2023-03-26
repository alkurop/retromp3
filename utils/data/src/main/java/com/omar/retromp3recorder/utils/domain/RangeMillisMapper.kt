package com.omar.retromp3recorder.utils.domain

import com.omar.retromp3recorder.domain.FromToMillis
import com.omar.retromp3recorder.domain.PlayerRange

fun PlayerRange.toFromToMillis(durationMillis: Long): FromToMillis {
    val rangeMultiplier = if (this.max == 0) 1 else durationMillis / this.max
    val fromMillis = if (this.from == 0) 0 else this.from * rangeMultiplier
    val toMillis = if (this.to == 0) 0 else this.to * rangeMultiplier
    return FromToMillis(fromMillis, toMillis)
}
