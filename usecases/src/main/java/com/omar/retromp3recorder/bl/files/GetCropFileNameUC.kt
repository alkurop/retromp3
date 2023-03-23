package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.utils.domain.DirPathProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class GetCropFileNameUC @Inject constructor(
    private val dirPathProvider: DirPathProvider,
)  {
    private var coroutineContext: Job = Job()
    suspend fun execute(originalFileName: String): NewNameSuggestion {
        coroutineContext.cancelAndJoin()
        coroutineContext = Job()
        return withContext(coroutineContext) {
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
            var newFilePath: String?
            var newName: String?
            var file: File?
            do {
                newCropCount++
                newName = "${originalName}$CROP_SUFFIX$newCropCount"
                newFilePath = "$path/$newName.$extension"
                file = File(newFilePath)
            } while (
                file?.exists() != false
            )
            NewNameSuggestion(
                path = newFilePath!!,
                name = newName!!
            )
        }
    }
}

private const val CROP_SUFFIX = "_CROP_"
