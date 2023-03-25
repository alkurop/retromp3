package com.omar.retromp3recorder.domain

data class CropRequest(
    val range: FromToMillis,
    val original: ExistingFileWrapper,
    val newFileNameSuggestion: NewNameSuggestion
)

data class CropResponse(
    val isSuccess: Boolean
)
