package com.omar.retromp3recorder.app.ui.files.selector

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.FileDbEntity

object SelectorView {
    data class State(
        val selectedFile: String?,
        val items: LiveData<PagedList<FileDbEntity>>?
    )

    sealed class Input {
        data class ItemSelected(val item: ExistingFileWrapper) : Input()
    }

    sealed class Output {
        data class FileList(val items: LiveData<PagedList<FileDbEntity>>) : Output()
        data class CurrentFile(val filePath: String?) : Output()
    }
}