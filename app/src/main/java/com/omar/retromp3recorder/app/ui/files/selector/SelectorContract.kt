package com.omar.retromp3recorder.app.ui.files.selector

import androidx.paging.PagingData
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.ItemsSource
import kotlinx.coroutines.flow.Flow

object SelectorContract {
    data class State(
        val selectedFile: String? = null,
        val itemsPaging: Flow<PagingData<ExistingFileWrapper>>? = null,
        val itemsSource: ItemsSource? = null
    )

    sealed class Input {
        data class ItemSelected(val item: ExistingFileWrapper) : Input()
    }

    sealed class Output {
        data class FileListNew(val itemsSource: ItemsSource) : Output()
        data class CurrentFile(val filePath: String?) : Output()
    }
}