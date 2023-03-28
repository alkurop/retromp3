package com.omar.retromp3recorder.bl.files

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IncrementFileNameUCSuspend @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val scopeJobWrapper: ScopeJobWrapper,
) {
    suspend fun execute() {

        withContext(scopeJobWrapper.coroutineContext) {
            val current = sharedPreferences.getInt(SharedPrefsKeys.FILE_NAME, 1)
            sharedPreferences.edit().putInt(SharedPrefsKeys.FILE_NAME, current + 1).apply()
        }
    }
}
