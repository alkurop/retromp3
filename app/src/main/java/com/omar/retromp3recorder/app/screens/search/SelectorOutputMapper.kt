package com.omar.retromp3recorder.app.screens.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.ItemsLoadRequest
import com.omar.retromp3recorder.storage.db.ItemsSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

object SelectorOutputMapper {
    fun Flow<SelectorContract.Output>.mapToState(): Flow<SelectorContract.State> {
        return this.scan(SelectorContract.State()) { oldState, output ->
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
