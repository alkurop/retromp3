package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.utils.DirPathProvider
import io.reactivex.rxjava3.core.Single
import java.io.File
import javax.inject.Inject

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
            val path = dirPathProvider.providerDirPath()
            var result: String?
            var file: File?
            do {
                newCropCount++
                result = "$path/${originalName}$CROP_SUFFIX$newCropCount.$extension"
                file = File(result)
            } while (
                file?.exists()?.not() != false
            )
            result!!
        }
    }
}

private const val CROP_SUFFIX = "__CROP__"
