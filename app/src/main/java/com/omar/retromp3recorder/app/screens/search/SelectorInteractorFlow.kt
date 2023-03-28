package com.omar.retromp3recorder.app.screens.search

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.files.SetCurrentFileUC
import com.omar.retromp3recorder.storage.db.DatabasePagingProvider
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SelectorInteractorFlow @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val pagingProvider: DatabasePagingProvider,
    private val setCurrentFileUC: SetCurrentFileUC,
    dispatcher: CoroutineDispatcher
) : Interactor<SelectorContract.Input, SelectorContract.Output>(dispatcher) {

    override fun listRepos(): List<Flow<SelectorContract.Output>> {
        return listOf(
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
            }
        }
    }
}

