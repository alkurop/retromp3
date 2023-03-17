package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.main.components.joined_progress.JoinedProgressViewMapper.mapOutputToStateFlow
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinedProgressViewModelFlow @Inject constructor(
    interactor: JoinedProgressInteractorFlow
) : ViewModel() {

    val state: StateFlow<JoinedProgressView.State>
    private val inputFlow = MutableSharedFlow<JoinedProgressView.In>()

    init {
        state = interactor.processIO(inputFlow)
            .mapOutputToStateFlow()
            .stateInViewModel(this, JoinedProgressView.State())
    }

    fun onEvent(event: JoinedProgressView.In) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
