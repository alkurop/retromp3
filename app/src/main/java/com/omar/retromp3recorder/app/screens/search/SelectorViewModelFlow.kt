package com.omar.retromp3recorder.app.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.search.SelectorOutputMapper.mapToState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectorViewModelFlow : ViewModel() {
    private val _state = MutableStateFlow(SelectorContract.State())
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<SelectorContract.Input>()

    @Inject
    lateinit var interactor: SelectorInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)

        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .mapToState()
                .collect { _state.value = it }

        }
    }

    fun onEvent(event: SelectorContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
