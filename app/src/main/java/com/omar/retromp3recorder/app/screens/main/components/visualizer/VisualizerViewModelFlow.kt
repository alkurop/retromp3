package com.omar.retromp3recorder.app.screens.main.components.visualizer

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.visualizer.VisualizerOutputMapper.mapOutputToStateFlow
import com.omar.retromp3recorder.app.utils.stateInViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class VisualizerViewModelFlow : ViewModel() {
    val state: StateFlow<VisualizerView.State>

    @Inject
    lateinit var interactor: VisualizerInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        state = interactor.processIO()
            .mapOutputToStateFlow()
            .stateInViewModel(this, VisualizerView.State())
    }
}
