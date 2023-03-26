package com.omar.retromp3recorder.utils.domain

data class Optional<T>(
    val value: T?
) {
    fun hasValue(): Boolean {
        return value != null
    }

    companion object {
        fun <T> empty(): Optional<T> = Optional(null)
    }
}

fun <T> T?.toOptional(): Optional<T> = Optional(this)
