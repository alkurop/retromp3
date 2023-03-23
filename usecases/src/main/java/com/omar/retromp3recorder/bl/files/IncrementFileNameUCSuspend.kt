package com.omar.retromp3recorder.bl.files

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class IncrementFileNameUCSuspend @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : CoroutineScope {
    suspend fun execute() {
        withContext(coroutineContext) {
            val current = sharedPreferences.getInt(SharedPrefsKeys.FILE_NAME, 1)
            sharedPreferences.edit().putInt(SharedPrefsKeys.FILE_NAME, current + 1).apply()
        }
    }

    override val coroutineContext: CoroutineContext = Job()
}
