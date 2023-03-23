package com.omar.retromp3recorder.bl.files

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IncrementFileNameUCSuspend @Inject constructor(
    private val sharedPreferences: SharedPreferences
)  {
    private var coroutineContext: Job = Job()
    suspend fun execute() {
        coroutineContext.cancelAndJoin()
        coroutineContext = Job()
        withContext(coroutineContext) {
            val current = sharedPreferences.getInt(SharedPrefsKeys.FILE_NAME, 1)
            sharedPreferences.edit().putInt(SharedPrefsKeys.FILE_NAME, current + 1).apply()
        }
    }
}
