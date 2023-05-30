package com.omar.retromp3recorder.app.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.search.SelectorOutputMapper.mapToState
import com.omar.retromp3recorder.app.utils.cacheInViewModel
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectorViewModel @Inject constructor(
    interactor: SelectorInteractor
) : ViewModel() {
    private val inputFlow = MutableSharedFlow<SelectorContract.Input>()

    val state = interactor.processIO(inputFlow)
        .mapToState()
        .map {
            val flow = it.flow.cacheInViewModel(this)
            it.copy(flow = flow)
        }
        .stateInViewModel(this, SelectorContract.State())


    fun onEvent(event: SelectorContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
