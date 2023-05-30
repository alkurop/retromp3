package com.omar.retromp3recorder.app.screens.home.components.audio_controls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.home.components.audio_controls.AudioControlsOutputMapper.mapOutputToStateFlow
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioControlsViewModel @Inject constructor(
    interactor: AudioControlsInteractor
) : ViewModel() {
    private val inputFlow = MutableSharedFlow<AudioControlsView.Input>()

    val state = interactor.processIO(inputFlow)
        .mapOutputToStateFlow()
        .stateInViewModel(this, AudioControlsView.State())

    fun onEvent(event: AudioControlsView.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
