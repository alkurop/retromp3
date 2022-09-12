package com.omar.retromp3recorder.utils

data class Optional<T>(
    val value: T?
) {
    companion object {
        fun <T> empty(): Optional<T> = Optional(null)
    }
}

fun <T> T?.toOptional(): Optional<T> = Optional(this)