package com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop.CropMapper.mapToState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CropViewModelFlow : ViewModel() {

    val state: StateFlow<CropContract.State>
    private val inputFlow = MutableSharedFlow<CropContract.Input>()

    @Inject
    lateinit var interactor: CropInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        state = interactor.processIO(inputFlow)
            .mapToState()
            .stateIn(viewModelScope, Lazily, CropContract.State())
    }

    fun emit(event: CropContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
