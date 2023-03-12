package com.omar.retromp3recorder.bl.files

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.utils.domain.DirPathProvider
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Get file path, and then create filename from incremented shared pref
 */
class GetNewFileNameUC @Inject constructor(
    private val dirPathProvider: DirPathProvider,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(): Single<String> {
        return Single.fromCallable {
            val int = sharedPreferences.getInt(
                SharedPrefsKeys.FILE_NAME,
                1
            )
            "${dirPathProvider.providerDirPath()}/audiorecord_$int.mp3"
        }
    }
}
