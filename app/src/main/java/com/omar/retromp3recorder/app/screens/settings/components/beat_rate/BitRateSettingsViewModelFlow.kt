package com.omar.retromp3recorder.app.screens.settings.components.beat_rate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.utils.stateInViewModel
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BitRateSettingsViewModelFlow @Inject constructor(
    interactor: BitRateSettingsInteractorFlow
) : ViewModel() {

    private val inputFlow = MutableSharedFlow<Mp3VoiceRecorder.BitRate>()

    val state = interactor.processIO(viewModelScope, inputFlow)        .stateInViewModel(this, Mp3VoiceRecorder.BitRate.values()[0])

    fun onEvent(event: Mp3VoiceRecorder.BitRate) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
