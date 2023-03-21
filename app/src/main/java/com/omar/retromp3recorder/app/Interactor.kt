package com.omar.retromp3recorder.app

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
abstract class Interactor<Input, Output>(private val dispatcher: CoroutineDispatcher) {
    fun processIO(upstream: Flow<Input> = flowOf()): Flow<Output> {
        return listOf(
            listenToRepos(),
            upstream.processInputs(),
        ).merge().flowOn(dispatcher)
    }

    protected abstract fun listRepos(): List<Flow<Output>>

    private fun Flow<Input>.processInputs(): Flow<Output> {
        return this.flatMapMerge { event -> flow { launchUseCase(event) } }
    }

    protected abstract suspend fun FlowCollector<Output>.launchUseCase(input: Input)


    private fun listenToRepos(): Flow<Output> {
        return listRepos().merge()
    }
}
