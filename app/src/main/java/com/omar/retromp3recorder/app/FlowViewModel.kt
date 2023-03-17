package com.omar.retromp3recorder.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
abstract class FlowViewModel<Input, Output, State>(
    val dispatcher: CoroutineDispatcher
) : ViewModel() {

    protected abstract val defaultState: State

    private val inputFlow = MutableSharedFlow<Input>()

    //lazy to use open val in constructor
    private val _state by lazy { MutableStateFlow(defaultState) }
    val state by lazy { _state.asStateFlow() }

    fun emit(input: Input) {
        viewModelScope.launch { inputFlow.emit(input) }
    }

    init {
        viewModelScope.launch {
            processIO(inputFlow)
                .mapToState()
                .collect { _state.value = it }

        }
    }

    private fun processIO(upstream: Flow<Input>): Flow<Output> {
        return listOf(
            listenToRepos(),
            upstream.processInputs(),
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<Input>.processInputs(): Flow<Output> {
        return this.flatMapMerge { event ->
            flow {
                getUsecase(event)
            }
        }
    }

    protected abstract fun listenToRepos(): Flow<Output>

    protected abstract fun Flow<Output>.mapToState(): Flow<State>

    protected abstract suspend fun FlowCollector<Output>.getUsecase(event: Input)

}
