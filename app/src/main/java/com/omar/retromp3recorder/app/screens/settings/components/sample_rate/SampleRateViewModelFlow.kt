package com.omar.retromp3recorder.app.screens.settings.components.sample_rate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SampleRateViewModelFlow : ViewModel() {
    private val _state = MutableStateFlow(Mp3VoiceRecorder.SampleRate.values()[0])
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<Mp3VoiceRecorder.SampleRate>()

    @Inject
    lateinit var interactor: SampleRateInteractorFlow

    init {
        App.appComponent.inject(this)
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .collect { _state.value = it }

        }
    }

    fun onEvent(event:Mp3VoiceRecorder.SampleRate) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
