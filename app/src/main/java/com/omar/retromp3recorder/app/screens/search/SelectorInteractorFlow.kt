package com.omar.retromp3recorder.app.screens.search

import com.omar.retromp3recorder.bl.files.SetCurrentFileUC
import com.omar.retromp3recorder.storage.db.DatabasePagingProvider
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SelectorInteractorFlow @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val pagingProvider: DatabasePagingProvider,
    private val setCurrentFileUC: SetCurrentFileUC,
    private val dispatcher: CoroutineDispatcher
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    fun processIO(upstream: Flow<SelectorContract.Input>): Flow<SelectorContract.Output> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<SelectorContract.Input>.processInputs(): Flow<SelectorContract.Output> {
        return this.transform { event ->
            when (event) {
                is SelectorContract.Input.ItemSelected -> {
                    launch {
                        setCurrentFileUC.execute(event.item)
                    }
                }
            }
        }
    }

    private fun listenToRepos(): Flow<SelectorContract.Output> {
        return listOf(
            currentFileRepo.flow().map {
                SelectorContract.Output.CurrentFile(it.value!!.path)
            },
            flowOf(pagingProvider.provideItemSource()).map {
                SelectorContract.Output.FileListNew(itemsSource = it)
            },
        ).merge()
    }
}

