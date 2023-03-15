package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.AudioControlsOutputMapper.mapOutputToStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioControlsViewModelFlow : ViewModel() {
    val state: StateFlow<AudioControlsView.State>
    private val inputFlow = MutableSharedFlow<AudioControlsView.Input>()

    @Inject
    lateinit var interactor: AudioControlsInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        state = interactor.processIO(inputFlow)
            .mapOutputToStateFlow()
            .stateIn(viewModelScope, SharingStarted.Lazily, AudioControlsView.State())
    }

    fun onEvent(event: AudioControlsView.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
