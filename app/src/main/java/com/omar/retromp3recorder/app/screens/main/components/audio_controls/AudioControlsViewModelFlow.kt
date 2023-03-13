package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.AudioControlsOutputMapper.mapOutputToStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioControlsViewModelFlow : ViewModel() {
    private val _state = MutableStateFlow(AudioControlsView.State())
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<AudioControlsView.Input>()

    @Inject
    lateinit var interactor: AudioControlsInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .mapOutputToStateFlow()
                .collect { _state.value = it }

        }
    }

    fun onEvent(event: AudioControlsView.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
