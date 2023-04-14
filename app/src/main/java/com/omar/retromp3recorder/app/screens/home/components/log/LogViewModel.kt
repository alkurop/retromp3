package com.omar.retromp3recorder.app.screens.home.components.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.home.components.log.LogOutputMapper.mapOutputToState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    interactor: LogInteractor
) : ViewModel() {
    val state = interactor.processIO(viewModelScope, flowOf())
        .mapOutputToState()
        .stateIn(viewModelScope, SharingStarted.Lazily, LogView.State())
}
