package com.omar.retromp3recorder.app.screens.main.components.visualizer

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.screens.main.components.visualizer.VisualizerOutputMapper.mapOutputToStateFlow
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VisualizerViewModelFlow @Inject constructor(
    interactor: VisualizerInteractorFlow
) : ViewModel() {
    val state: StateFlow<VisualizerView.State> = interactor.processIO()
        .mapOutputToStateFlow()
        .stateInViewModel(this, VisualizerView.State())
}
