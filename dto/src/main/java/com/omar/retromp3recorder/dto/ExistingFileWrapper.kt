package com.omar.retromp3recorder.dto

import java.io.File

sealed class FileWrapper(open val path: String)
data class FutureFileWrapper(
    override val path: String
) : FileWrapper(path)

data class ExistingFileWrapper(
    override val path: String,
    val createTimedStamp: Long,
    val modifiedTimestamp: Long = 0L,
    val wavetable: Wavetable?,
    val length: Long?
) : FileWrapper(path)

fun String.toFutureFileWrapper(): FutureFileWrapper =
    FutureFileWrapper(this)

fun File.toFileWrapper(): ExistingFileWrapper =
    ExistingFileWrapper(
        this.path,
        this.lastModified(),
        this.lastModified(),
        null,
        0
    )

fun Wavetable.ofZeros(): Boolean {
    return this.data.sum() == 0
}