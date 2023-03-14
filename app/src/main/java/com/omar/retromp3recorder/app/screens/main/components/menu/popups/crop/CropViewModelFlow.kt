package com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop.CropMapper.mapToState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CropViewModelFlow : ViewModel() {

    private val _state = MutableStateFlow(CropContract.State())
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<CropContract.Input>()

    @Inject
    lateinit var interactor: CropInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .mapToState()
                .collect { _state.value = it }

        }
    }


    fun emit(event: CropContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
