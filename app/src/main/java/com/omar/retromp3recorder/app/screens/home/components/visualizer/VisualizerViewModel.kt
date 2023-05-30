package com.omar.retromp3recorder.app.screens.home.components.visualizer

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.screens.home.components.visualizer.VisualizerOutputMapper.mapOutputToStateFlow
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisualizerViewModel @Inject constructor(
    interactor: VisualizerInteractor
) : ViewModel() {
    val state = interactor.processIO()
        .mapOutputToStateFlow()
        .stateInViewModel(this, VisualizerView.State())
}
