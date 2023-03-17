package com.omar.retromp3recorder.app.screens.settings.components.audio_source

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioSourceViewModelFlow @Inject constructor(
    private val interactor: AudioSourceInteractorFlow
) : ViewModel() {
    private val _state = MutableStateFlow(Mp3VoiceRecorder.AudioSourcePref.values()[0])
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<Mp3VoiceRecorder.AudioSourcePref>()

    init {
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .collect { _state.value = it }

        }
    }

    fun onEvent(event:Mp3VoiceRecorder.AudioSourcePref) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
