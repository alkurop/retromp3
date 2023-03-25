package com.omar.retromp3recorder.share

import com.github.alkurop.stringerbell.Stringer
import kotlinx.coroutines.flow.Flow
import java.io.File

interface Sharer {
    suspend fun share(file: File)
    fun flow(): Flow<Event>

    sealed class Event {
        data class SharingOk constructor(val message: Stringer) : Event()
        data class Error constructor(val error: Stringer) : Event()
    }
}

