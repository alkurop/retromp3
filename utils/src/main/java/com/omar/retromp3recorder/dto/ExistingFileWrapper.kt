package com.omar.retromp3recorder.dto

import java.io.File

sealed class FileWrapper(open val path: String) {
}

data class FutureFileWrapper(
    override val path: String
) : FileWrapper(path)

data class ExistingFileWrapper(
    val id: Long,
    override val path: String,
    val createTimedStamp: Long,
    val modifiedTimestamp: Long = 0L,
    val wavetable: Wavetable?,
    val length: Long?,
) : FileWrapper(path) {
    val name = path.getNameOnly()
}

fun String.toFutureFileWrapper(): FutureFileWrapper =
    FutureFileWrapper(this)

fun File.toFileWrapper(): ExistingFileWrapper =
    ExistingFileWrapper(
        0,
        this.path,
        this.lastModified(),
        this.lastModified(),
        null,
        0
    )

fun Wavetable?.isEmpty(): Boolean {
    return this == null || this.data.sum() == 0
}

private fun String.getNameOnly(): String {
    return this.split("/").last().split(".").first()
}