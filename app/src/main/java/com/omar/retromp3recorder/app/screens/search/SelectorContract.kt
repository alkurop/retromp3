package com.omar.retromp3recorder.app.screens.search

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.ItemsSource
import kotlinx.coroutines.flow.Flow

object SelectorContract {
    @Stable
    data class State(
        val selectedFile: String? = null,
        val itemsPaging: Flow<PagingData<ExistingFileWrapper>>? = null,
        val itemsSource: ItemsSource? = null,
        val shouldDismiss: Boolean = false
    )

    sealed class Input {
        data class ItemSelected(val item: ExistingFileWrapper) : Input()
    }

    sealed class Output {
        data class FileListNew(val itemsSource: ItemsSource) : Output()
        data class CurrentFile(val filePath: String?) : Output()
        object Dismiss : Output()
    }
}
