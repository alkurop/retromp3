package com.omar.retromp3recorder.app.screens.main.components.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.main.components.log.LogOutputMapper.mapOutputToState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LogViewModelFlow @Inject constructor(
    interactor: LogInteractorFlow
) : ViewModel() {
    val state: StateFlow<LogView.State> = interactor.processIO()
        .mapOutputToState()
        .stateIn(viewModelScope, SharingStarted.Lazily, LogView.State())
}
