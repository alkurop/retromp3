package com.omar.retromp3recorder.app

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
abstract class Interactor<Input, Output>(private val dispatcher: CoroutineDispatcher) {

    fun processIO(upstream: Flow<Input> = flowOf()): Flow<Output> {

        return (listRepos() + upstream.processInputs())
            .merge()
            .flowOn(dispatcher)
    }

    protected abstract fun listRepos(): List<Flow<Output>>

    private fun Flow<Input>.processInputs(): Flow<Output> {
        return this.flatMapMerge { event ->
            flow {
               withContext(dispatcher) {
                    launchUseCase(event)
                }
            }
        }
    }

    protected abstract suspend fun launchUseCase(input: Input)

    private fun listenToRepos(): Flow<Output> {
        return listRepos().merge()
    }
}
