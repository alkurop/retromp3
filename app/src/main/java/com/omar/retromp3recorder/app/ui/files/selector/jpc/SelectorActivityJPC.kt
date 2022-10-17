package com.omar.retromp3recorder.app.ui.files.selector.jpc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.omar.retromp3recorder.app.ui.files.selector.jpc.layout.SearchLayout


class SelectorActivityJPC : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchLayout()
        }
    }
}