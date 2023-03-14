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
    dispatcher: CoroutineDispatcher
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()


    fun processIO(upstream: Flow<SelectorContract.Input>): Flow<SelectorContract.Output> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().distinctUntilChanged()
    }

    private fun Flow<SelectorContract.Input>.processInputs(): Flow<SelectorContract.Output> {
        return this.transform { event ->
            launch {
                when (event) {
                    is SelectorContract.Input.ItemSelected -> {
                        setCurrentFileUC.execute(event.item).blockingAwait()
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

