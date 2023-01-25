package com.omar.retromp3recorder.app.ui.files.selector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.omar.retromp3recorder.app.ui.files.selector.layout.SearchScreenLayout
import com.omar.retromp3recorder.app.ui.theme.RetroTheme


class SelectorActivityJPC : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetroTheme {
                SearchScreenLayout()
            }
        }
    }
}
