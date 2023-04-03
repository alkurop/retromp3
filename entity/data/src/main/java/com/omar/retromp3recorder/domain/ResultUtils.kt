package com.omar.retromp3recorder.domain


fun <T> Throwable.toResult(): Result<T> = Result.failure(this)

fun <T> T.toResult(): Result<T> = Result.success(this)

fun <In, Out> Result<In>.mapError(): Result<Out> {
    return this.exceptionOrNull()!!.toResult()
}

fun <Prev, Next> Result<Prev>.chain(action: (Prev) -> Result<Next>): Result<Next> {
    return if (this.isSuccess) {
        val result = getOrNull()!!
        action(result)
    } else {
        this.exceptionOrNull()!!.toResult()
    }
}

suspend fun <Prev, Next> Result<Prev>.chainSuspend(action: suspend (Prev) -> Result<Next>): Result<Next> {
    return if (this.isSuccess) {
        val result = getOrNull()!!
        action(result)
    } else {
        this.exceptionOrNull()!!.toResult()
    }
}
