package com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete.DeleteFileOutputMapper.mapToState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteFileViewModelFlow : ViewModel() {

    private val _state = MutableStateFlow(DeleteFileContract.State())
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<DeleteFileContract.Input>()

    @Inject
    lateinit var interactor: DeleteFileInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .mapToState()
                .collect { _state.value = it }

        }
    }


    fun emit(event: DeleteFileContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
