package com.omar.retromp3recorder.app.screens.main.components.visualizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.visualizer.VisualizerOutputMapper.mapOutputToStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class VisualizerViewModelFlow : ViewModel() {
    private val _state = MutableStateFlow(VisualizerView.State())
    val state = _state.asStateFlow()

    @Inject
    lateinit var interactor: VisualizerInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        viewModelScope.launch {
            interactor.processIO().mapOutputToStateFlow()
                .collect {
                    _state.value = it
                }
        }
    }
}
