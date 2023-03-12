package com.omar.retromp3recorder.utils.domain

import android.content.Context
import linc.com.amplituda.Amplituda
import javax.inject.Inject

class AmplitudaDealer @Inject constructor(
    private val context: Context
) {
    fun createAmplituda(): Amplituda {
        return Amplituda(context)
    }
}
