package com.omar.retromp3recorder.app.screens.main.components.rangebar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RangeBarViewModelFlow : ViewModel() {
    private val _state = MutableStateFlow<RangeBarView.State>(RangeBarView.State.Hidden)
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<RangeBarView.Input>()

    @Inject
    lateinit var interactor: RangeBarInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .collect { _state.value = it }
        }
    }

    fun onEvent(event: RangeBarView.Input) {
        viewModelScope.launch {
            inputFlow.emit(event)
        }
    }
}
