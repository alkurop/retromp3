package com.omar.retromp3recorder.utils

import linc.com.amplituda.Amplituda

interface AmplitudaDealer {
    fun createAmplituda(): Amplituda
}