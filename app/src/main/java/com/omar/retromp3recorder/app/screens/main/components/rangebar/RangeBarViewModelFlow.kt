package com.omar.retromp3recorder.app.screens.main.components.rangebar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class RangeBarViewModelFlow : ViewModel() {
    val state: StateFlow<RangeBarView.State>
    private val inputFlow = MutableSharedFlow<RangeBarView.Input>()

    @Inject
    lateinit var interactor: RangeBarInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        state = interactor.processIO(inputFlow)
            .stateIn(viewModelScope, SharingStarted.Lazily, RangeBarView.State.Hidden)
    }

    fun onEvent(event: RangeBarView.Input) {
        viewModelScope.launch {
            inputFlow.emit(event)
        }
    }
}
