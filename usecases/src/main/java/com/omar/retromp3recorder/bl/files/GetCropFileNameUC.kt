package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.utils.DirPathProvider
import io.reactivex.rxjava3.core.Single
import java.io.File
import javax.inject.Inject

/**
 * Get file path, and then create filename from incremented shared pref
 */
class GetCropFileNameUC @Inject constructor(
    private val dirPathProvider: DirPathProvider
) {
    fun execute(originalFileName: String): Single<String> {
        return Single.fromCallable {
            val pathSplit = originalFileName.split("/")
            val fileName = pathSplit.last()
            val fileSplit = fileName.split(".")
            val name = fileSplit.first()
            val extension = fileSplit.last()
            val cropNameSplit = name.split(CROP_SUFFIX)
            val originalName = cropNameSplit.first()
            val cropSuffix = if (cropNameSplit.size > 1) cropNameSplit.last() else "0"
            val previousCropCount = cropSuffix.toIntOrNull() ?: 0

            var newCropCount = previousCropCount

            var result: String?
            var file: File?
            do {
                newCropCount++
                val mutablePath = pathSplit.toMutableList()
                mutablePath.removeLast()
                mutablePath.add("${originalName}$CROP_SUFFIX$newCropCount.$extension")
                result = mutablePath.joinToString("/")
                file = File(result)
            } while (
                file?.exists()?.not() != false
            )
            result!!
        }
    }
}

private const val CROP_SUFFIX = "__CROP__"
