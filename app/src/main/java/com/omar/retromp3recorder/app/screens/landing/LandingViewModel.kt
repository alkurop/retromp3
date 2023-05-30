package com.omar.retromp3recorder.app.screens.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.bl.system.StartupUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val startupUC: StartupUC,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    suspend fun load() {
        withContext(dispatcher) {
            startupUC.execute()
        }
    }
}
