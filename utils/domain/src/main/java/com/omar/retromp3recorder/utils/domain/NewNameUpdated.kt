package com.omar.retromp3recorder.utils.domain

import com.omar.retromp3recorder.domain.NewNameSuggestion


fun Pair<NewNameSuggestion, String>.updateName(): NewNameSuggestion {
    val oldPath = this.first.path
    val pathSplit = oldPath.split("/")
    val fileName = pathSplit.last()
    val fileSplit = fileName.split(".")


    val extension = fileSplit.last()

    val dir = pathSplit.subList(0, pathSplit.size - 1).joinToString("/")
    val fullPath = "$dir/${this.second}.$extension"
    return NewNameSuggestion(
        path = fullPath,
        name = this.second
    )
}
