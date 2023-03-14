package com.omar.retromp3recorder.data.mock

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import java.util.Random

object MockFileFactory {
    fun giveFile(): ExistingFileWrapper {
        val id = Random().nextLong()
        return ExistingFileWrapper(
            id = id,
            path = "$id.path",
            createTimedStamp = id,
            modifiedTimestamp = id,
            wavetable = null,
            length = id
        )
    }

}
