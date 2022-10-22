package com.omar.retromp3recorder.app.ui.files.selector

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.ItemsLoadRequest
import com.omar.retromp3recorder.storage.db.ItemsSource
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction
import kotlinx.coroutines.flow.Flow

object SelectorOutputMapper {
    fun mapOutputToState(): ObservableTransformer<SelectorContract.Output, SelectorContract.State> =
        ObservableTransformer { upstream: Observable<SelectorContract.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<SelectorContract.State, SelectorContract.Output, SelectorContract.State> =
        BiFunction { oldState: SelectorContract.State, output: SelectorContract.Output ->
            when (output) {
                is SelectorContract.Output.FileList -> {
                    oldState.copy(items = output.items)
                }
                is SelectorContract.Output.QueryChanged -> {
                    val itemsSource = oldState.itemsSource
                    if (itemsSource != null)
                        oldState.copy(
                            itemsPaging = itemsSource.createFlow(output.query)
                        ) else oldState
                }
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
            }
        }

    private fun getDefaultViewModel() = SelectorContract.State(
        items = null,
        // selected file has to be here,   in case selection comes before list
        selectedFile = null
    )
}

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