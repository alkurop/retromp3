package com.omar.retromp3recorder.app

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
abstract class Interactor<Input, Output>(private val dispatcher: CoroutineDispatcher) {
    private lateinit var parentScope: CoroutineScope

    fun processIO(parentScope: CoroutineScope, upstream: Flow<Input> = flowOf()): Flow<Output> {
        this.parentScope = parentScope

        return listOf(
            listenToRepos(),
            upstream.processInputs(),
        ).merge().flowOn(dispatcher)
    }

    protected abstract fun listRepos(): List<Flow<Output>>

    private fun Flow<Input>.processInputs(): Flow<Output> {
        return this.flatMapMerge { event ->
            flow {
                parentScope.launch(dispatcher) {
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
