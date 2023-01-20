package com.omar.retromp3recorder.app.uiutils

import android.content.Context
import android.text.SpannableString
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.utils.toSpannableStringWithSmallMillis
import com.omar.retromp3recorder.utils.toTimeDisplay

object TimeDisplay {
    fun Long.toDisplay(context: Context): SpannableString =
        this.toTimeDisplay()
            .toSpannableStringWithSmallMillis(context, R.style.Control_Normal_Millis)

}


