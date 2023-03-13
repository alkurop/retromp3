package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.joined_progress.JoinedProgressViewMapper.mapOutputToStateFlow
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JoinedProgressViewModelFlow : ViewModel() {
    private val _state = MutableStateFlow(JoinedProgressView.State())
    val state = _state.asStateFlow()
    private val inputFlow = MutableSharedFlow<JoinedProgressView.In>()

    @Inject
    lateinit var interactor: JoinedProgressInteractorFlow
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.getComponent().inject(this)
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .mapOutputToStateFlow()
                .collect{_state.value = it}
        }
    }

    fun onEvent(event: JoinedProgressView.In) {
        viewModelScope.launch { inputFlow.emit(event) }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
