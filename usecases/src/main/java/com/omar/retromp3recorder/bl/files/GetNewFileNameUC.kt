package com.omar.retromp3recorder.bl.files

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.utils.DirPathProvider
import io.reactivex.rxjava3.core.Single
import java.util.*
import javax.inject.Inject

/**
 * Get file path, and then create filename from incremented shared pref
 */
class GetNewFileNameUC @Inject constructor(
    private val dirPathProvider: DirPathProvider,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(): Single<String> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val dateFormat =
            "${day}-${month}-${year}_$hours-$minutes"

        return Single.fromCallable {
            val int = sharedPreferences.getInt(
                SharedPrefsKeys.FILE_NAME,
                1
            )
            "${dirPathProvider.providerDirPath()}/${dateFormat}($int).mp3"
        }
    }
}