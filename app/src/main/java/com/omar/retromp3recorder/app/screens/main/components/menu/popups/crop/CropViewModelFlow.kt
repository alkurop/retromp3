package com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop.CropMapper.mapToState
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropViewModelFlow @Inject constructor(
    interactor: CropInteractorFlow
) : ViewModel() {

    val state: StateFlow<CropContract.State>
    private val inputFlow = MutableSharedFlow<CropContract.Input>()


    init {
        state = interactor.processIO(inputFlow)
            .mapToState()
            .stateInViewModel(this, CropContract.State())
    }

    fun emit(event: CropContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
