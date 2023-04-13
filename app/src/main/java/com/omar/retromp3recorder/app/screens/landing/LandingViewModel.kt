package com.omar.retromp3recorder.app.screens.landing

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.bl.system.StartupUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val startupUC: StartupUC
) : ViewModel() {
    suspend fun load() {
        startupUC.execute()
    }
}
