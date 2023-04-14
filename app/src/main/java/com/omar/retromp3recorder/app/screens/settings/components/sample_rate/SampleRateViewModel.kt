package com.omar.retromp3recorder.app.screens.settings.components.sample_rate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.utils.stateInViewModel
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleRateViewModel @Inject constructor(
    interactor: SampleRateInteractor
) : ViewModel() {

    private val inputFlow = MutableSharedFlow<Mp3VoiceRecorder.SampleRate>()

    val state = interactor.processIO(viewModelScope, inputFlow)
        .stateInViewModel(this, Mp3VoiceRecorder.SampleRate.values()[0])

    fun onEvent(event: Mp3VoiceRecorder.SampleRate) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
