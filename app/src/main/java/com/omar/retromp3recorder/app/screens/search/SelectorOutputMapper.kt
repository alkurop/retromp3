package com.omar.retromp3recorder.app.screens.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omar.retromp3recorder.domain.ExistingFileWrapper
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

    private fun getDefaultViewModel() = SelectorContract.State()
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
