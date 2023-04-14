package com.omar.retromp3recorder.app.screens.home.components.menu.popups.rename

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.home.components.menu.popups.rename.RenameFileOutputMapper.mapToState
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RenameFileViewModelFlow @Inject constructor(
    interactor: RenameFileInteractorFlow
) : ViewModel() {

    private val inputFlow = MutableSharedFlow<RenameFileContract.Input>()

    val state = interactor.processIO(inputFlow, viewModelScope)
        .mapToState()
        .stateInViewModel(this, RenameFileContract.State())

    fun onEvent(event: RenameFileContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
