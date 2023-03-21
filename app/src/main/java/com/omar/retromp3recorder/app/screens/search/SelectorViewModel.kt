package com.omar.retromp3recorder.app.screens.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omar.retromp3recorder.app.FlowViewModel
import com.omar.retromp3recorder.bl.files.SetCurrentFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.DatabasePagingProvider
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.ItemsLoadRequest
import com.omar.retromp3recorder.storage.db.ItemsSource
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SelectorViewModel @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val pagingProvider: DatabasePagingProvider,
    private val setCurrentFileUC: SetCurrentFileUC,
    dispatcher: CoroutineDispatcher
) : FlowViewModel<SelectorContract.Input, SelectorContract.Output, SelectorContract.State>(
    dispatcher
) {
    private val shouldDismiss = MutableSharedFlow<Boolean>()

    override val initialState: SelectorContract.State = SelectorContract.State()

    override val repos: List<Flow<SelectorContract.Output>>
        get() = listOf(
            shouldDismiss.map { SelectorContract.Output.Dismiss },
            currentFileRepo.flow().map {
                val filePath = requireNotNull(it.value?.path) {
                    "path cannot be null"
                }
                SelectorContract.Output.CurrentFile(filePath)
            },
            flowOf(pagingProvider.provideItemSource()).map {
                SelectorContract.Output.FileListNew(itemsSource = it)
            }
        )

    override val launchUsecase: suspend FlowCollector<SelectorContract.Output>.(SelectorContract.Input) -> Unit =
        { event ->
            when (event) {
                is SelectorContract.Input.ItemSelected -> {
                    setCurrentFileUC.execute(event.item)
                    shouldDismiss.emit(true)
                }
            }
        }
    override val stateMapper: (SelectorContract.State, SelectorContract.Output) -> SelectorContract.State =
        { oldState, output ->
            when (output) {
                is SelectorContract.Output.FileListNew -> {
                    oldState.copy(
                        itemsSource = output.itemsSource,
                        itemsPaging = output.itemsSource.createFlow("")
                    )
                }
                is SelectorContract.Output.CurrentFile -> {
                    val selectedFile = output.filePath
                    oldState.copy(
                        selectedFile = selectedFile
                    )
                }
                SelectorContract.Output.Dismiss -> {
                    oldState.copy(shouldDismiss = true)
                }
            }
        }

    init {
        launch()
    }
}

//JPC does not like changing source of data, so I'm filtering on the view side.
//keeping query db cuz I like how it works, but no current usecase for it
private fun ItemsSource.createFlow(query: String): Flow<PagingData<ExistingFileWrapper>> {
    return Pager(
        initialKey = ItemsLoadRequest(query, page = 0),
        config = PagingConfig(
            pageSize = FileDbEntityDao.LOAD_SIZE,

            enablePlaceholders = false
        )
    ) {
        this
    }.flow
}
