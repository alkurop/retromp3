package com.omar.retromp3recorder.utils.platform.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface Repo<In : Any, Out:Any> {
    suspend fun emit(input: In)

    fun flow(): Flow<Out>
}

suspend fun <Out : Any> Repo<*, Out>.first() = flow().first()
