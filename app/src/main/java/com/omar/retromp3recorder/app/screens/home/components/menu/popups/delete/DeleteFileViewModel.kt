package com.omar.retromp3recorder.app.screens.home.components.menu.popups.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.home.components.menu.popups.delete.DeleteFileOutputMapper.mapToState
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DeleteFileViewModel @Inject constructor(
    interactor: DeleteFileInteractor
) : ViewModel() {

    private val inputFlow = MutableSharedFlow<DeleteFileContract.Input>()

    val state = interactor.processIO(inputFlow, viewModelScope)
        .mapToState()
        .stateInViewModel(this, DeleteFileContract.State())

    fun emit(event: DeleteFileContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
