package com.omar.retromp3recorder.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
abstract class FlowViewModel<Input, Output, State>(
    protected val dispatcher: CoroutineDispatcher
) : ViewModel() {

    protected val inputFlow = MutableSharedFlow<Input>()

    abstract val initialState: State

    fun emit(input: Input) {
        viewModelScope.launch { inputFlow.emit(input) }
    }

    fun processIO(upstream: Flow<Input>): Flow<Output> {
        return (repos + upstream.processInputs()).merge().flowOn(dispatcher)
    }

    private fun Flow<Input>.processInputs(): Flow<Output> {
        return this.flatMapMerge { event ->
            flow {
                launchUsecase(event)
            }
        }
    }

    abstract val repos: List<Flow<Output>>

    abstract val launchUsecase: FlowCollector<Output>.(Input) -> Unit

    abstract val stateMapper: (State, Output) -> State

    protected fun Flow<Output>.mapToState(): Flow<State> {
        return this.scan(initialState) { oldState, output -> stateMapper(oldState, output) }
    }
}
