package com.omar.retromp3recorder.app.screens.main.components.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.log.LogOutputMapper.mapOutputToState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class LogViewModelFlow : ViewModel() {

    val state: StateFlow<LogView.State>

    @Inject
    lateinit var interactor: LogInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
            state = interactor.processIO()
                .mapOutputToState()
                .stateIn(viewModelScope, SharingStarted.Lazily, LogView.State())
    }
}
