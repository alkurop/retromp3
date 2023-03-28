package com.omar.retromp3recorder.app.screens.search

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.utils.domain.LoadingState
import kotlinx.coroutines.flow.Flow

typealias SearchItemsLoadingState = LoadingState<SearchItemsFlow>

typealias SearchItemsFlow = Flow<PagingData<ExistingFileWrapper>>

object SelectorContract {

    @Stable
    data class State(
        val selectedFile: ExistingFileWrapper? = null,
        val flow: SearchItemsLoadingState = LoadingState.Loading(),
    )

    sealed class Input {
        data class ItemSelected(val item: ExistingFileWrapper) : Input()
        data class SetQuery(val query: String) : Input()
    }

    sealed class Output {
        data class CurrentFile(val filePath: ExistingFileWrapper?) : Output()
        data class CurrentFlow(val flow: SearchItemsLoadingState) : Output()
    }
}
