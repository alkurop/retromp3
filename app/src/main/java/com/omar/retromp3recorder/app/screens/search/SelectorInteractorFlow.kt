package com.omar.retromp3recorder.app.screens.search

import com.omar.retromp3recorder.bl.files.SetCurrentFileUC
import com.omar.retromp3recorder.storage.db.DatabasePagingProvider
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class SelectorInteractorFlow @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val pagingProvider: DatabasePagingProvider,
    private val setCurrentFileUC: SetCurrentFileUC,
    private val dispatcher: CoroutineDispatcher
) {
    private val shouldDismiss = MutableSharedFlow<Boolean>()

    fun processIO(upstream: Flow<SelectorContract.Input>): Flow<SelectorContract.Output> {
        return listOf(
            listenToRepos(),
            upstream.processInputs(),
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<SelectorContract.Input>.processInputs(): Flow<SelectorContract.Output> {
        return this.flatMapMerge { event ->
            flow {
                when (event) {
                    is SelectorContract.Input.ItemSelected -> {
                        setCurrentFileUC.execute(event.item)
                        shouldDismiss.emit(true)
                    }
                }
            }
        }
    }

    private fun listenToRepos(): Flow<SelectorContract.Output> {
        return listOf(
            shouldDismiss.map { SelectorContract.Output.Dismiss },
            currentFileRepo.flow().map {
                val filePath = requireNotNull(it.value?.path) {
                    "path cannot be null"
                }
                SelectorContract.Output.CurrentFile(filePath)
            },
            flowOf(pagingProvider.provideItemSource()).map {
                SelectorContract.Output.FileListNew(itemsSource = it)
            },
        ).merge()
    }
}

