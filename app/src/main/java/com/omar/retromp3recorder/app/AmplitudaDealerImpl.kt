package com.omar.retromp3recorder.app

import android.content.Context
import com.omar.retromp3recorder.utils.AmplitudaDealer
import linc.com.amplituda.Amplituda
import javax.inject.Inject

class AmplitudaDealerImpl @Inject constructor(
    private val context: Context
) : AmplitudaDealer {
    override fun createAmplituda(): Amplituda {
        return Amplituda(context)
    }
}