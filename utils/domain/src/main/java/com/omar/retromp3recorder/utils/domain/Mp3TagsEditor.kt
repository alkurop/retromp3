package com.omar.retromp3recorder.utils.domain

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.mpatric.mp3agic.ID3v1Tag
import com.mpatric.mp3agic.Mp3File
import com.omar.retromp3recorder.domain.RecordingTags
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class Mp3TagsEditor @Inject constructor(
    private val context: Context,
    private val recordingTagsDefaultsProvider: RecordingTagsDefaultProvider,
    private val fileEmptyChecker: FileEmptyChecker
) {
    fun setTags(filepath: String, tags: RecordingTags) {
        try {
            if (isTagsWork().not()) return
            if (fileEmptyChecker.isFileEmpty(filepath).not()) {
                val mp3File = Mp3File(filepath)
                mp3File.id3v1Tag = ID3v1Tag().apply {
                    year = tags.year
                    artist = tags.artist
                    title = tags.title
                }
                val temp = "${context.cacheDir}/temp.mp3"
                mp3File.save(temp)
                try {
                    File(temp).copyTo(File(filepath), overwrite = true)
                    File(temp).delete()
                } catch (e: FileAlreadyExistsException) {
                    Timber.e(e)
                }
                val newFile = Mp3File(filepath)
                val id3v1Tag = newFile.id3v1Tag
                Timber.d("Tag $id3v1Tag,")
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getFilenameFromPath(filePath: String): String {
        return filePath.split("/").last().split("-").last().split(".").first()
    }

    fun getTags(filepath: String): RecordingTags? {
        val defaults = recordingTagsDefaultsProvider.provideDefaults()
        val titleFromFileName = getFilenameFromPath(filepath)
        return if (isTagsWork() && fileEmptyChecker.isFileEmpty(filepath).not()) {
            try {
                val id3v1Tag = Mp3File(filepath).id3v1Tag ?: ID3v1Tag()
                id3v1Tag.run {
                    RecordingTags(
                        year = year ?: defaults.year,
                        artist = artist ?: defaults.artist,
                        title = title ?: titleFromFileName
                    )
                }
            } catch (e: Throwable) {
                Timber.e(e)
                null
            }
        } else defaults.copy(title = titleFromFileName)
    }
}

private const val VERSION_TAGS_WORK = Build.VERSION_CODES.O_MR1

@ChecksSdkIntAtLeast(api = VERSION_TAGS_WORK)
private fun isTagsWork() = Build.VERSION.SDK_INT >= VERSION_TAGS_WORK
