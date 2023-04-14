package com.omar.retromp3recorder.app.screens.home.components.audio_controls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.home.components.audio_controls.AudioControlsOutputMapper.mapOutputToStateFlow
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioControlsViewModel @Inject constructor(
    interactor: AudioControlsInteractorFlow
) : ViewModel() {
    val state: StateFlow<AudioControlsView.State>
    private val inputFlow = MutableSharedFlow<AudioControlsView.Input>()

    init {
        state = interactor.processIO(inputFlow, viewModelScope)
            .mapOutputToStateFlow()
            .stateInViewModel(this, AudioControlsView.State())
    }

    fun onEvent(event: AudioControlsView.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
