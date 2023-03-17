package com.omar.retromp3recorder.utils.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import linc.com.amplituda.Amplituda
import javax.inject.Inject

class AmplitudaDealer @Inject constructor(
   @ApplicationContext private val context: Context
) {
    fun createAmplituda(): Amplituda {
        return Amplituda(context)
    }
}
