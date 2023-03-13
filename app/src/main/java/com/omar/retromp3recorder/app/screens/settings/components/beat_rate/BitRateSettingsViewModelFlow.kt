package com.omar.retromp3recorder.app.screens.settings.components.beat_rate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BitRateSettingsViewModelFlow : ViewModel() {
    private val _state = MutableStateFlow(Mp3VoiceRecorder.BitRate.values()[0])
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<Mp3VoiceRecorder.BitRate>()

    @Inject
    lateinit var interactor: BitRateSettingsInteractorFlow

    init {
        App.appComponent.inject(this)
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .collect { _state.value = it }

        }
    }

    fun onEvent(event:Mp3VoiceRecorder.BitRate) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
