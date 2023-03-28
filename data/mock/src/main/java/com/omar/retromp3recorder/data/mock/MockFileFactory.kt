package com.omar.retromp3recorder.data.mock

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.FutureFileWrapper
import java.util.Random

object MockFileFactory {
    fun giveExistingFile(id: Long = Random().nextLong()): ExistingFileWrapper {

        return ExistingFileWrapper(
            id = id,
            path = "$id.path",
            createTimedStamp = id,
            modifiedTimestamp = id,
            wavetable = null,
            length = id
        )
    }

    fun giveFutureFile(): FutureFileWrapper {
        val id = Random().nextLong()
        return FutureFileWrapper(path = "$id.path")
    }

}
