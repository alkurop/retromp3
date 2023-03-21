package com.omar.retromp3recorder.app.screens.search

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.files.SetCurrentFileUC
import com.omar.retromp3recorder.storage.db.DatabasePagingProvider
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SelectorInteractorFlow @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val pagingProvider: DatabasePagingProvider,
    private val setCurrentFileUC: SetCurrentFileUC,
    dispatcher: CoroutineDispatcher
) : Interactor<SelectorContract.Input, SelectorContract.Output>(dispatcher) {
    private val shouldDismiss = MutableSharedFlow<Boolean>()

    override fun listRepos(): List<Flow<SelectorContract.Output>> {
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
        )
    }

    override suspend fun FlowCollector<SelectorContract.Output>.launchUseCase(input: SelectorContract.Input) {
        when (input) {
            is SelectorContract.Input.ItemSelected -> {
                setCurrentFileUC.execute(input.item)
                shouldDismiss.emit(true)
            }
        }
    }
}

