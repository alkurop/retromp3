package com.omar.retromp3recorder.bl.files

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.utils.domain.DirPathProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Get file path, and then create filename from incremented shared pref
 */
class GetNewFileNameUCSuspend @Inject constructor(
    private val dirPathProvider: DirPathProvider,
    private val sharedPreferences: SharedPreferences
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = Job()

    suspend fun execute(): String {
        val int = withContext(coroutineContext) {
            sharedPreferences.getInt(
                SharedPrefsKeys.FILE_NAME,
                1
            )
        }
        return "${dirPathProvider.providerDirPath()}/audiorecord_$int.mp3"

    }
}
