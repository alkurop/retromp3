package com.omar.retromp3recorder.app.screens.settings.components.audio_source

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.utils.stateInViewModel
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioSourceViewModelFlow @Inject constructor(
    interactor: AudioSourceInteractorFlow
) : ViewModel() {

    private val inputFlow = MutableSharedFlow<Mp3VoiceRecorder.AudioSourcePref>()

    val state = interactor.processIO(inputFlow, viewModelScope)
        .stateInViewModel(this, Mp3VoiceRecorder.AudioSourcePref.values()[0])


    fun onEvent(event: Mp3VoiceRecorder.AudioSourcePref) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
