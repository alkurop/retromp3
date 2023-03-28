package com.omar.retromp3recorder.app.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.search.SelectorOutputMapper.mapToState
import com.omar.retromp3recorder.app.utils.cacheInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectorViewModelFlow @Inject constructor(
    private val interactor: SelectorInteractorFlow
) :ViewModel() {
    private val _state = MutableStateFlow(SelectorContract.State())
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<SelectorContract.Input>()


    init {
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .mapToState()
                .collect {
                    val flow = it.flow.cacheInViewModel(this)
                    _state.value = it .copy(flow = flow)
                }

        }
    }

    fun onEvent(event: SelectorContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
