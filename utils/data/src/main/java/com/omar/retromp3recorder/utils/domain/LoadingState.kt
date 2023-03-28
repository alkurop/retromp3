package com.omar.retromp3recorder.utils.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.CancellationException


/**
 * Represents loading state, after a data request was made to the DataLayer.
 *
 * The view layer should use this state to modify layout.
 * @param T expected data type
 */
sealed class LoadingState<T> {
    /**
     * Data request is in progress
     */
    class Loading<T> : LoadingState<T>()

    /**
     * Data request is success with data [data]
     */
    data class Success<T>(val data: T) : LoadingState<T>()

    /**
     * Data request failed with [cause]
     */
    data class Error<T>(val cause: Throwable) : LoadingState<T>()
}


/**
 * Executes Retrofit response, wrapped into a [Flow]
 * The flow will emit Loading, Success and Error loading state
 */
fun <T> executeLoadingRequest(action: suspend () -> Result<T>): Flow<LoadingState<T>> =
    flow {
        emit(LoadingState.Loading())
        val result = action()
        val data = result.getOrNull()

        if (data != null) {
            emit(LoadingState.Success(data))
        } else {
            val exception =
                requireNotNull(result.exceptionOrNull()) { "Failed with null exception" }
            emit(LoadingState.Error(exception))
        }
    }

fun <T> LoadingState<T>.getOrNull(): T? = when (this) {
    is LoadingState.Success -> this.data
    else -> null
}


/**
 * Load data with a try catch.
 * @param T - Return type
 * @param action - function that should be executed to get the data
 * @return - result wrapped into [Result]
 */
suspend fun <T> loadCatching(action: suspend () -> T): Result<T> {
    return try {
        Result.success(action.invoke())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

fun <T> T.toLoadingState() = LoadingState.Success(this)
