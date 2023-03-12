package com.omar.retromp3recorder.share

import com.github.alkurop.stringerbell.Stringer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.io.File

interface Sharer {
    fun share(file: File): Completable
    fun observeEvents(): Observable<Event>

    sealed class Event {
        data class SharingOk constructor(val message: Stringer) : Event()
        data class Error constructor(val error: Stringer) : Event()
    }
}

