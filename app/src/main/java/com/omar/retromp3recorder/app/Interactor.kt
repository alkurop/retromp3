package com.omar.retromp3recorder.app

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
abstract class Interactor<Input, Output>(private val dispatcher: CoroutineDispatcher) {
    private lateinit var parentScope: CoroutineScope

    fun processIO(parentScope: CoroutineScope, upstream: Flow<Input> = flowOf()): Flow<Output> {
        this.parentScope = parentScope

        return listOf(
            listenToRepos(),
            upstream.processInputs(),
        )
            .merge()
            .flowOn(dispatcher)
    }

    protected abstract fun listRepos(): List<Flow<Output>>

    private fun Flow<Input>.processInputs(): Flow<Output> {
        return this.flatMapMerge { event ->
            channelFlow {
                parentScope.launch {
                    launchUseCase(event)
                }
            }
        }
    }

    protected abstract suspend fun ProducerScope<Output>.launchUseCase(input: Input)


    private fun listenToRepos(): Flow<Output> {
        return listRepos().merge()
    }
}
