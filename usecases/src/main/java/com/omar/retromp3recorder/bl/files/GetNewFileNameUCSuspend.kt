package com.omar.retromp3recorder.bl.files

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.utils.domain.DirPathProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Get file path, and then create filename from incremented shared pref
 */
class GetNewFileNameUCSuspend @Inject constructor(
    private val dirPathProvider: DirPathProvider,
    private val sharedPreferences: SharedPreferences
) {
    private var coroutineContext: Job = Job()

    suspend fun execute(): String {
        coroutineContext.cancelAndJoin()
        coroutineContext = Job()
        val int = withContext(coroutineContext) {
            sharedPreferences.getInt(
                SharedPrefsKeys.FILE_NAME,
                1
            )
        }
        return "${dirPathProvider.providerDirPath()}/audiorecord_$int.mp3"
    }
}
