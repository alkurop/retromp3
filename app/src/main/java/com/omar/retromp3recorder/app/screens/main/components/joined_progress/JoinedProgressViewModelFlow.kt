package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.joined_progress.JoinedProgressViewMapper.mapOutputToStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class JoinedProgressViewModelFlow : ViewModel() {

    val state: StateFlow<JoinedProgressView.State>
    private val inputFlow = MutableSharedFlow<JoinedProgressView.In>()

    @Inject
    lateinit var interactor: JoinedProgressInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        state = interactor.processIO(inputFlow)
            .mapOutputToStateFlow()
            .stateIn(viewModelScope, SharingStarted.Lazily, JoinedProgressView.State())
    }

    fun onEvent(event: JoinedProgressView.In) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
